package com.example.scanp.ui.history

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.ui.Alignment
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.example.scanp.R
import com.example.scanp.data.domain.ProductDomain
import com.example.scanp.util.ProductTypeEnum
import com.example.scanp.viewmodel.HistoryViewModel

@Composable
fun HistoryScreen(
    viewModel: HistoryViewModel,
    onItemClick: (String) -> Unit = {},
    onNavigateBack: () -> Unit = {}
) {
    val history by viewModel.history.collectAsStateWithLifecycle()

    HistoryScreenContent(
        historyList = history,
        onItemClick = onItemClick,
        onNavigateBack = onNavigateBack
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreenContent(
    historyList: List<ProductDomain>,
    onItemClick: (String) -> Unit,
    onNavigateBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Scan History") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        if (historyList.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Text("No scan history available.")
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                items(historyList) { entry ->
                    HistoryItem(entry, onItemClick)
                    HorizontalDivider(Modifier, DividerDefaults.Thickness, DividerDefaults.color)
                }
            }
        }
    }
}

@Composable
private fun HistoryItem(entry: ProductDomain, onItemClick: (String) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable { onItemClick(entry.barcode) }
    ) {
        AsyncImage(
            model = entry.imageUrl,
            contentDescription = null,
            placeholder = painterResource(R.drawable.product),
            error = painterResource(R.drawable.product),
            modifier = Modifier
                .size(50.dp)
        )
        Column(modifier = Modifier
            .padding(start = 8.dp)
            .fillMaxHeight()) {
            entry.name?.let { Text(text = it, style = MaterialTheme.typography.bodyLarge) }
            Text(text = entry.barcode, style = MaterialTheme.typography.bodySmall)
        }

    }
}

@Preview
@Composable
fun HistoryScreenPreview() {
    HistoryScreenContent(
        historyList = listOf(
            ProductDomain(
                barcode = "1234567890123",
                name = "Organic Whole Milk",
                productType = ProductTypeEnum.FOOD,
                ingredients = "Milk",
                energyKcal = 60.0,
                proteins = 3.3,
                carbs = 4.8,
                sugars = 4.8,
                fat = 3.5,
                saturatedFat = 2.2,
                fibre = 0.0,
                salt = 0.1,
                imageUrl = "https://example.com/milk.jpg",
                isSaved = true
            ),
            ProductDomain(
                barcode = "9876543210987",
                name = "Whole Wheat Bread",
                productType = ProductTypeEnum.FOOD,
                ingredients = "Whole wheat flour, water, yeast, salt",
                energyKcal = 247.0,
                proteins = 13.0,
                carbs = 41.0,
                sugars = 4.0,
                fat = 3.0,
                saturatedFat = 0.5,
                fibre = 7.0,
                salt = 1.0,
                imageUrl = "https://example.com/bread.jpg",
                isSaved = false
            ),
            ProductDomain(
                barcode = "5555555555555",
                name = "Greek Yogurt",
                productType = ProductTypeEnum.FOOD,
                ingredients = "Milk, live active cultures",
                energyKcal = 59.0,
                proteins = 10.0,
                carbs = 3.6,
                sugars = 3.2,
                fat = 0.4,
                saturatedFat = 0.1,
                fibre = 0.0,
                salt = 0.1,
                imageUrl = "https://example.com/yogurt.jpg",
                isSaved = true
            )
        ),
        onItemClick = { barcode -> },
        onNavigateBack = { }
    )
}

@Preview
@Composable
fun HistoryScreenEmptyPreview() {
    HistoryScreenContent(
        historyList = emptyList(),
        onItemClick = { },
        onNavigateBack = { }
    )
}