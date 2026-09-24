package com.example.scanfood.ui.history

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.foundation.clickable
import androidx.compose.material3.Icon
import androidx.compose.ui.Alignment
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.example.scanfood.viewmodel.HistoryViewModel
import org.koin.androidx.compose.koinViewModel
import com.example.scanfood.data.domain.ProductDomain
import com.example.scanfood.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    viewModel: HistoryViewModel,
    onItemClick: (String) -> Unit = {},
    onNavigateBack: () -> Unit = {}
) {
    val history by viewModel.history.collectAsStateWithLifecycle()

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
        if (history.isEmpty()) {
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
                items(history) { entry ->
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