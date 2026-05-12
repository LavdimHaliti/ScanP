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

@OptIn(ExperimentalGetImage::class)
@Composable
fun ScannerView(
    onBarcodeDetected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val lifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current
    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }
    val executor = remember { Executors.newSingleThreadExecutor() }
    val barcodeScanner = remember { BarcodeScanning.getClient() }
    val barcodeDetected = remember { AtomicBoolean(false) }
    var hasCameraPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
        )
    }
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        hasCameraPermission = granted
    }

    LaunchedEffect(Unit) {
        if (!hasCameraPermission) {
            launcher.launch(Manifest.permission.CAMERA)
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        if (hasCameraPermission) {
            AndroidView(
                factory = { ctx ->
                    val previewView = PreviewView(ctx)
                    val cameraProvider = cameraProviderFuture.get()
                    val preview = Builder().build().also {
                        it.surfaceProvider = previewView.surfaceProvider
                    }
                    val imageAnalysis = ImageAnalysis.Builder()
                        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                        .build()

                    imageAnalysis.setAnalyzer(executor) { imageProxy ->
                        if (barcodeDetected.get()) {
                            imageProxy.close()
                            return@setAnalyzer
                        }
                        val mediaImage = imageProxy.image
                        if (mediaImage != null) {
                            val image = InputImage.fromMediaImage(
                                mediaImage,
                                imageProxy.imageInfo.rotationDegrees
                            )
                            barcodeScanner.process(image)
                                .addOnSuccessListener { barcodes ->
                                    if (!barcodeDetected.get()) {
                                        for (barcode in barcodes) {
                                            barcode.rawValue?.let { value ->
                                                if (barcodeDetected.compareAndSet(false, true)) {
                                                    onBarcodeDetected(value)
                                                }
                                            }
                                        }
                                    }
                                }
                                .addOnCompleteListener {
                                    imageProxy.close()
                                }
                        } else {
                            imageProxy.close()
                        }
                    }

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

            ScanFrameOverlay()
        }
    }
}

@Composable
fun ScanFrameOverlay() {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val canvasWidth = size.width
        val canvasHeight = size.height

        val frameWidth = canvasWidth * 0.7f
        val frameHeight = frameWidth * 0.5f
        val left = (canvasWidth - frameWidth) / 2
        val top = (canvasHeight - frameHeight) / 2 - 100.dp.toPx()
        val cornerLength = 40.dp.toPx()
        val strokeWidth = 3.dp.toPx()
        val color = Color.White

        drawRect(
            color = Color.Black.copy(alpha = 0.5f),
            topLeft = Offset(0f, 0f),
            size = Size(canvasWidth, top)
        )

        drawRect(
            color = Color.Black.copy(alpha = 0.5f),
            topLeft = Offset(0f, top),
            size = Size(left, frameHeight)
        )

        drawRect(
            color = Color.Black.copy(alpha = 0.5f),
            topLeft = Offset(left + frameWidth, top),
            size = Size(canvasWidth - left - frameWidth, frameHeight)
        )

        drawRect(
            color = Color.Black.copy(alpha = 0.5f),
            topLeft = Offset(0f, top + frameHeight),
            size = Size(canvasWidth, canvasHeight - top - frameHeight)
        )

        drawLine(color, Offset(left, top), Offset(left + cornerLength, top), strokeWidth)
        drawLine(color, Offset(left, top), Offset(left, top + cornerLength), strokeWidth)

        drawLine(color, Offset(left + frameWidth, top), Offset(left + frameWidth - cornerLength, top), strokeWidth)
        drawLine(color, Offset(left + frameWidth, top), Offset(left + frameWidth, top + cornerLength), strokeWidth)

        drawLine(color, Offset(left, top + frameHeight), Offset(left + cornerLength, top + frameHeight), strokeWidth)
        drawLine(color, Offset(left, top + frameHeight), Offset(left, top + frameHeight - cornerLength), strokeWidth)

        drawLine(color, Offset(left + frameWidth, top + frameHeight), Offset(left + frameWidth - cornerLength, top + frameHeight), strokeWidth)
        drawLine(color, Offset(left + frameWidth, top + frameHeight), Offset(left + frameWidth, top + frameHeight - cornerLength), strokeWidth)
    }
}
