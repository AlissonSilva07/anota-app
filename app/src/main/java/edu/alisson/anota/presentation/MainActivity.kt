package edu.alisson.anota.presentation

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.ui.graphics.toArgb
import dagger.hilt.android.AndroidEntryPoint
import edu.alisson.anota.presentation.navigation.AppNavigation
import edu.alisson.anota.presentation.ui.theme.AnotaTheme
import edu.alisson.anota.presentation.ui.theme.bg
import edu.alisson.anota.presentation.ui.theme.primary

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.auto(
                android.graphics.Color.TRANSPARENT,
                android.graphics.Color.TRANSPARENT
            ),
            navigationBarStyle = SystemBarStyle.auto(
                android.graphics.Color.TRANSPARENT,
                android.graphics.Color.TRANSPARENT
            ),

        )
        setContent {
            AnotaTheme(
                dynamicColor = false
            ) {
                AppNavigation()
            }
        }
    }
}