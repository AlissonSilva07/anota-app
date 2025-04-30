package edu.alisson.anota.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import dagger.hilt.android.AndroidEntryPoint
import edu.alisson.anota.presentation.navigation.AppNavigation
import edu.alisson.anota.presentation.ui.theme.AnotaTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AnotaTheme(
                dynamicColor = false
            ) {
                AppNavigation()
            }
        }
    }
}