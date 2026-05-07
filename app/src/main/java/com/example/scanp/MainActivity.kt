package com.example.scanp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.scanp.navigation.ScanPApp
import com.example.scanp.ui.theme.ScanPTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ScanPTheme {
                Surface (modifier = Modifier.fillMaxSize()) {
                    ScanPApp()
                }
            }
        }
    }
}