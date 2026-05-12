package com.example.scanp.ui.scan

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.scanp.data.domain.ProductDomain
import com.example.scanp.ui.uistate.ScanUiState
import com.example.scanp.viewmodel.ScanViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun ScanScreen(
    navigateToNutrition: (String) -> Unit,
    navigateToIngredientList: (String) -> Unit,
    navigateToHistory: () -> Unit,
    viewModel: ScanViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var scannedBarcode by remember { mutableStateOf<String?>(null) }
    var showBarcodeDialog by remember { mutableStateOf(false) }
    var scanKey by remember { mutableStateOf(0) }

    ScanScreenContent(
        uiState = uiState,
        scannedBarcode = scannedBarcode,
        onScanBarScanned = { scannedBarcode = it },
        fetchProduct = { barcode -> viewModel.fetchProduct(barcode) },
        navigateToNutrition = { barcode -> navigateToNutrition(barcode) },
        navigateToIngredientList = { barcode -> navigateToIngredientList(barcode) },
        navigateToHistory = { navigateToHistory() },
        resetScan = {
            viewModel.resetScan()
            scanKey++
        },
        clearError = { viewModel.clearError() },
        onShowBarcodeDialog = { showBarcodeDialog = true },
        showBarcodeDialog = showBarcodeDialog,
        barcodeDialog = { showBarcodeDialog = it },
        scanKey = scanKey
    )

}

@Composable
fun ScanScreenContent(
    uiState: ScanUiState,
    scannedBarcode: String?,
    onScanBarScanned: (String?) -> Unit,
    fetchProduct: (String) -> Unit,
    navigateToNutrition: (String) -> Unit,
    navigateToIngredientList: (String) -> Unit,
    navigateToHistory: () -> Unit,
    resetScan: () -> Unit,
    clearError: () -> Unit,
    onShowBarcodeDialog: () -> Unit,
    showBarcodeDialog: Boolean,
    barcodeDialog: (Boolean) -> Unit,
    scanKey: Int
) {
    LaunchedEffect(scannedBarcode) {
        scannedBarcode?.let { barcode ->
            fetchProduct(barcode)
            onScanBarScanned(null)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (showBarcodeDialog) {
            BarcodeEntryDialog(
                onDismiss = { barcodeDialog(false) },
                onConfirm = { barcode ->
                    barcodeDialog(false)
                    onScanBarScanned(barcode)
                }
            )
        }

        key(scanKey) {
            ScannerView(
                onBarcodeDetected = { barcode ->
                    if (!uiState.isLoading && uiState.product == null) {
                        onScanBarScanned(barcode)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.6f)
            )
        }

        when {
            uiState.isLoading -> {
                LoadingView()
            }

            uiState.product != null -> {
                ProductResultCard(
                    product = uiState.product,
                    onNutritionClick = {
                        navigateToNutrition(uiState.product.barcode)
                    },
                    onIngredientsClick = {
                        navigateToIngredientList(uiState.product.barcode)
                    },
                    onScanAgain = { resetScan() }
                )
            }

            uiState.error != null -> {
                ErrorView(
                    error = uiState.error,
                    onRetry = {
                        uiState.lastBarcode?.let { barcode ->
                            fetchProduct(barcode)
                        }
                    },
                    onDismiss = {
                        clearError()
                        resetScan()
                    }
                )
            }

            else -> {
                Spacer(modifier = Modifier.weight(0.4f))
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = { onShowBarcodeDialog() },
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Enter Barcode")
            }
            Button(
                onClick = { navigateToHistory() },
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("History")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun LoadingView() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator()
        Spacer(modifier = Modifier.height(16.dp))
        Text("Looking up product...", style = MaterialTheme.typography.bodyMedium)
    }
}

@Composable
fun ProductResultCard(
    product: ProductDomain,
    onNutritionClick: () -> Unit,
    onIngredientsClick: () -> Unit,
    onScanAgain: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = product.name ?: "Unknown Product",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Barcode: ${product.barcode}",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                NutritionItem("Calories", "${product.energyKcal?.toInt() ?: 0} kcal")
                NutritionItem("Protein", "%.2f g".format(product.proteins ?: 0.0))
                NutritionItem("Carbs", "%.2f g".format(product.carbs ?: 0.0))
                NutritionItem("Fat", "%.2f g".format(product.fat ?: 0.0))
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = onNutritionClick,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Nutritions")
                }
                Button(
                    onClick = onIngredientsClick,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Ingredients")
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            TextButton(
                onClick = onScanAgain,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text("Scan Again")
            }
        }
    }
}

@Composable
fun NutritionItem(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
        Text(label, fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable
fun ErrorView(error: String, onRetry: () -> Unit, onDismiss: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Error",
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onErrorContainer
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = error,
                color = MaterialTheme.colorScheme.onErrorContainer
            )
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(onClick = onRetry) {
                    Text("Retry")
                }
                TextButton(onClick = onDismiss) {
                    Text("Dismiss")
                }
            }
        }
    }
}

@Composable
fun BarcodeEntryDialog(
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    var barcodeText by remember { mutableStateOf("") }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Enter Barcode",
                    style = MaterialTheme.typography.headlineSmall
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("Enter the product barcode manually:")
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = barcodeText,
                    onValueChange = { barcodeText = it },
                    label = { Text("Barcode") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancel")
                    }
                    Button(
                        onClick = {
                            if (barcodeText.isNotBlank()) {
                                onConfirm(barcodeText.trim())
                            }
                        }
                    ) {
                        Text("OK")
                    }
                }
            }
        }
    }
}