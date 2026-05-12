package com.example.scanp.ui.nutrition

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.scanp.viewmodel.ProductDetailViewModel
import org.koin.androidx.compose.koinViewModel
import androidx.compose.foundation.layout.Arrangement
import com.example.scanp.ui.uistate.ProductDetailUiState

@Composable
fun NutritionDetailScreen(
    barcode: String,
    onNavigateBack: () -> Unit,
    viewModel: ProductDetailViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    NutritionContent(
        uiState = uiState,
        barcode = barcode,
        onNavigateBack = { onNavigateBack() },
        loadProduct = { barcode -> viewModel.loadProduct(barcode) }

    )
}

@Composable
fun NutritionContent(
    uiState: ProductDetailUiState,
    barcode: String,
    onNavigateBack: () -> Unit,
    loadProduct: (String) -> Unit
) {
    LaunchedEffect(barcode) {
        if (barcode.isNotEmpty()) {
            loadProduct(barcode)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .statusBarsPadding()
            .navigationBarsPadding(),
    ) {
        Text(
            text = "Nutrition Facts",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        when {
            uiState.isLoading -> {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            uiState.product != null -> {
                val product = uiState.product
                Text(
                    text = product.name ?: "Unknown Product",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "Barcode: ${product.barcode}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(24.dp))

                NutritionRow("Energy", "%.2f kcal".format(product.energyKcal ?: 0.0))
                NutritionRow("Protein", "%.2f g".format(product.proteins ?: 0.0))
                NutritionRow("Carbohydrates", "%.2f g".format(product.carbs ?: 0.0))
                NutritionRow("Sugars", "%.2f g".format(product.sugars ?: 0.0))
                NutritionRow("Fat", "%.2f g".format(product.fat ?: 0.0))
                NutritionRow("Saturated Fat", "%.2f g".format(product.saturatedFat ?: 0.0))
                NutritionRow("Fiber", "%.2f g".format(product.fibre ?: 0.0))
                NutritionRow("Salt", "%.2f g".format(product.salt ?: 0.0))
            }
            uiState.error != null -> {
                Text(
                    text = "Error: ${uiState.error}",
                    color = MaterialTheme.colorScheme.error
                )
            }
            else -> {
                Text(
                    text = "No product loaded. Please scan a product first.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = { onNavigateBack() },
            modifier = Modifier.fillMaxWidth(),
            shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp)
        ) {
            Text("Back to Scan")
        }
    }
}

@Composable
fun NutritionRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold
        )
    }
    HorizontalDivider()
}
