package com.example.scanp.ui.scan

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.OptIn
import androidx.camera.core.*
import androidx.camera.core.Preview.Builder
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.Executors

/**
 * Full-screen composable that displays a camera preview and continuously
 * analyses frames for barcodes. When a barcode is detected for the first time
 * (duplicates are suppressed), it invokes [onBarcodeDetected] and stops further
 * analysis to prevent multiple triggers.
 *
 * @param onBarcodeDetected callback invoked with the raw barcode string as soon
 *                          as a valid barcode is recognised.
 * @param modifier standard Compose modifier for the root layout.
 */
@OptIn(ExperimentalGetImage::class) // required by ML Kit's InputImage.fromMediaImage
@Composable
fun ScannerView(
    onBarcodeDetected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val lifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current

    // CameraX ProcessCameraProvider is a Future – we remember the Future so it
    // survives recomposition but is still fetched only once per composition.
    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }

    // Single-threaded executor used for the ML Kit barcode analyser; kept as a
    // remember {} to avoid spawning a new thread on every recomposition.
    val executor = remember { Executors.newSingleThreadExecutor() }

    // ML Kit barcode scanner client – stateless, safe to cache.
    val barcodeScanner = remember { BarcodeScanning.getClient() }

    // Atomic flag to ensure the callback fires exactly once per scan session.
    val barcodeDetected = remember { AtomicBoolean(false) }

    // Tracks whether the CAMERA permission has been granted. On composable entry
    // the value is set to the current permission status.
    var hasCameraPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) ==
                    PackageManager.PERMISSION_GRANTED
        )
    }

    // Permission launcher that updates `hasCameraPermission` when the user responds.
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        hasCameraPermission = granted
    }

    // If permission is missing, launch the request as a side effect exactly once
    // (LaunchedEffect(Unit)) at the start of composition.
    LaunchedEffect(Unit) {
        if (!hasCameraPermission) {
            launcher.launch(Manifest.permission.CAMERA)
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        // Only show the camera preview when permission is granted.
        if (hasCameraPermission) {
            // Standard CameraX preview using AndroidView to embed the view-based
            // PreviewView inside Compose.
            AndroidView(
                factory = { ctx ->
                    val previewView = PreviewView(ctx)

                    // Wait for the camera provider to be ready (synchronous get() is
                    // safe because we create the future with remember and it's already
                    // initialised by the time this lambda runs).
                    val cameraProvider = cameraProviderFuture.get()

                    // Build the preview use case and connect it to the PreviewView's
                    // surface provider.
                    val preview = Builder().build().also {
                        it.surfaceProvider = previewView.surfaceProvider
                    }

                    // Set up image analysis: keep only the latest frame when the
                    // analyser is busy, avoiding backlog.
                    val imageAnalysis = ImageAnalysis.Builder()
                        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                        .build()

                    // Each camera frame arrives here…
                    imageAnalysis.setAnalyzer(executor) { imageProxy ->
                        // If we already found a barcode, close the proxy immediately
                        // without doing any analysis.
                        if (barcodeDetected.get()) {
                            imageProxy.close()
                            return@setAnalyzer
                        }

                        val mediaImage = imageProxy.image
                        if (mediaImage != null) {
                            // Convert the raw camera image to an ML Kit InputImage,
                            // accounting for rotation.
                            val image = InputImage.fromMediaImage(
                                mediaImage,
                                imageProxy.imageInfo.rotationDegrees
                            )

                            // Process asynchronously: the barcode scanner returns a
                            // Task<List<Barcode>>.
                            barcodeScanner.process(image)
                                .addOnSuccessListener { barcodes ->
                                    // On success, check the flag again to avoid a race.
                                    if (!barcodeDetected.get()) {
                                        for (barcode in barcodes) {
                                            barcode.rawValue?.let { value ->
                                                // compareAndSet returns true only if the
                                                // flag was false before, ensuring a single
                                                // callback invocation.
                                                if (barcodeDetected.compareAndSet(false, true)) {
                                                    onBarcodeDetected(value)
                                                }
                                            }
                                        }
                                    }
                                }
                                .addOnCompleteListener {
                                    // Always close the image proxy when the ML Kit
                                    // processing finishes (success or failure).
                                    imageProxy.close()
                                }
                        } else {
                            // No image data, close immediately to free the buffer.
                            imageProxy.close()
                        }
                    }

                    // Bind all use cases to the lifecycle, selecting the default back
                    // camera and binding the preview + image analysis.
                    try {
                        cameraProvider.unbindAll()
                        cameraProvider.bindToLifecycle(
                            lifecycleOwner,
                            CameraSelector.DEFAULT_BACK_CAMERA,
                            preview,
                            imageAnalysis
                        )
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                    previewView
                },
                modifier = Modifier.fillMaxSize()
            )

            // Overlay the scanning frame visual guide on top of the camera preview.
            ScanFrameOverlay()
        }
    }
}

/**
 * Draws a semi-transparent overlay with a central transparent "scanning window"
 * and white corner brackets, giving the user a visual target to aim the camera.
 *
 * The transparent window is 70% of the canvas width and 50% of that width tall,
 * centred horizontally and shifted upwards by 100 dp.
 */
@Composable
fun ScanFrameOverlay() {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val canvasWidth = size.width
        val canvasHeight = size.height

        // Dimensions of the transparent scanning window.
        val frameWidth = canvasWidth * 0.7f
        val frameHeight = frameWidth * 0.5f
        // Position of the top-left corner of the window.
        val left = (canvasWidth - frameWidth) / 2
        val top = (canvasHeight - frameHeight) / 2 - 100.dp.toPx()

        // Corner bracket dimensions and style.
        val cornerLength = 40.dp.toPx()
        val strokeWidth = 3.dp.toPx()
        val color = Color.White

        // ---- Draw four semi-transparent rectangles around the window ----

        // Top rectangle (above the window).
        drawRect(
            color = Color.Black.copy(alpha = 0.5f),
            topLeft = Offset(0f, 0f),
            size = Size(canvasWidth, top)
        )

        // Left rectangle (beside the window).
        drawRect(
            color = Color.Black.copy(alpha = 0.5f),
            topLeft = Offset(0f, top),
            size = Size(left, frameHeight)
        )

        // Right rectangle (beside the window).
        drawRect(
            color = Color.Black.copy(alpha = 0.5f),
            topLeft = Offset(left + frameWidth, top),
            size = Size(canvasWidth - left - frameWidth, frameHeight)
        )

        // Bottom rectangle (below the window).
        drawRect(
            color = Color.Black.copy(alpha = 0.5f),
            topLeft = Offset(0f, top + frameHeight),
            size = Size(canvasWidth, canvasHeight - top - frameHeight)
        )

        // ---- Draw four corner brackets ----

        // Top-left corner (extending right and down).
        drawLine(color, Offset(left, top), Offset(left + cornerLength, top), strokeWidth)
        drawLine(color, Offset(left, top), Offset(left, top + cornerLength), strokeWidth)

        // Top-right corner (extending left and down).
        drawLine(color, Offset(left + frameWidth, top),
            Offset(left + frameWidth - cornerLength, top), strokeWidth)
        drawLine(color, Offset(left + frameWidth, top),
            Offset(left + frameWidth, top + cornerLength), strokeWidth)

        // Bottom-left corner (extending right and up).
        drawLine(color, Offset(left, top + frameHeight),
            Offset(left + cornerLength, top + frameHeight), strokeWidth)
        drawLine(color, Offset(left, top + frameHeight),
            Offset(left, top + frameHeight - cornerLength), strokeWidth)

        // Bottom-right corner (extending left and up).
        drawLine(color, Offset(left + frameWidth, top + frameHeight),
            Offset(left + frameWidth - cornerLength, top + frameHeight), strokeWidth)
        drawLine(color, Offset(left + frameWidth, top + frameHeight),
            Offset(left + frameWidth, top + frameHeight - cornerLength), strokeWidth)
    }
}
