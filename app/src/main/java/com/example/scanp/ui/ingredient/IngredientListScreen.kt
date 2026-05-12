package com.example.scanp.ui.ingredient

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.LinearProgressIndicator
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.scanp.ui.uistate.ProductDetailUiState
import com.example.scanp.viewmodel.ProductDetailViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IngredientListScreen(
    barcode: String,
    onNavigateBack: () -> Unit,
    viewModel: ProductDetailViewModel = koinViewModel()
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Ingredients") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            IngredientListContent(
                uiState = viewModel.uiState.collectAsStateWithLifecycle().value,
                barcode = barcode,
                loadProduct = { bc -> viewModel.loadProduct(bc) }
            )
        }
    }
    return

}

@Composable
fun IngredientListContent(
    uiState: ProductDetailUiState,
    barcode: String,
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
            .padding(start = 16.dp, end = 16.dp)
            .verticalScroll(rememberScrollState()),
    ) {
        when {
            uiState.isLoading -> {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            uiState.product != null -> {
                val product = uiState.product
                Text(
                    text = product.name ?: "Unknown Product",
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Health Analysis Section
                val healthResult = remember(product.ingredients) {
                    IngredientDatabase.analyze(product.ingredients)
                }
                HealthScoreCard(healthResult)

                Spacer(modifier = Modifier.height(16.dp))

                // Ingredients List
                Text(
                    text = "Ingredient List",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = product.ingredients ?: "No ingredient information available",
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Unhealthy Ingredients Section
                if (healthResult.unhealthyIngredients.isNotEmpty()) {
                    UnhealthyIngredientsSection(healthResult.unhealthyIngredients)
                    Spacer(modifier = Modifier.height(16.dp))
                }

                // Potential Risks Section
                if (healthResult.allRisks.isNotEmpty()) {
                    PotentialRisksSection(healthResult.allRisks)
                }
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
    }
}

@Composable
fun HealthScoreCard(healthResult: IngredientHealthResult) {
    val healthColor = when (healthResult.healthPercentage) {
        in 80..100 -> Color(0xFF4CAF50)
        in 60..79 -> Color(0xFF8BC34A)
        in 40..59 -> Color(0xFFFF9800)
        in 20..39 -> Color(0xFFFF5722)
        else -> Color(0xFFD32F2F)
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Health Score",
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "${healthResult.healthPercentage}%",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = healthColor
            )

            Text(
                text = healthResult.healthRating,
                style = MaterialTheme.typography.bodyMedium,
                color = healthColor,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(12.dp))

            LinearProgressIndicator(
            progress = { healthResult.healthPercentage / 100f },
            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp),
            color = healthColor,
            trackColor = MaterialTheme.colorScheme.surfaceVariant,
            strokeCap = ProgressIndicatorDefaults.LinearStrokeCap,
            )
        }
    }
}

@Composable
fun UnhealthyIngredientsSection(unhealthyIngredients: List<UnhealthyIngredient>) {
    Text(
        text = "Unhealthy Ingredients Found (${unhealthyIngredients.size})",
        style = MaterialTheme.typography.titleSmall,
        fontWeight = FontWeight.SemiBold,
        color = MaterialTheme.colorScheme.error
    )

    Spacer(modifier = Modifier.height(8.dp))

    unhealthyIngredients.forEach { ingredient ->
        UnhealthyIngredientItem(ingredient)
        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable
fun UnhealthyIngredientItem(ingredient: UnhealthyIngredient) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = ingredient.riskLevel.color.copy(alpha = 0.1f)
        ),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = ingredient.name,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Badge(
                    containerColor = ingredient.riskLevel.color
                ) {
                    Text(
                        text = ingredient.riskLevel.displayName,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                        color = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Category: ${ingredient.category}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = ingredient.description,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Composable
fun PotentialRisksSection(risks: List<String>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFFFF3E0)
        ),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = "Potential Health Risks",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFFFF6F00)
            )

            Spacer(modifier = Modifier.height(8.dp))

            risks.forEach { risk ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.Top
                ) {
                    Text(
                        text = "•",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFFFF6F00),
                        modifier = Modifier.padding(end = 4.dp)
                    )
                    Text(
                        text = risk,
                        color = Color(0xFF130A0A),
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.weight(1f)
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
            }
        }
    }
}
