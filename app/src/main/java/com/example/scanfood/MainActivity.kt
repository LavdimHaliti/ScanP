package com.example.scanfood

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.scanfood.navigation.NavigationRoot
import com.example.scanfood.ui.theme.ScanPTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ScanPTheme {
                Surface (modifier = Modifier.fillMaxSize()) {
                    NavigationRoot()
                }
            }
        }
    }
}