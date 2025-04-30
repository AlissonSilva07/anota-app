package edu.alisson.anota.presentation.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Devices.PIXEL_6
import androidx.compose.ui.tooling.preview.Preview
import edu.alisson.anota.presentation.ui.home.components.HomeHeader
import edu.alisson.anota.presentation.ui.theme.AnotaTheme

@Composable
fun HomeScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        HomeHeader()
    }
}

@Preview(
    device = PIXEL_6
)
@Composable
private fun HomeScreenPrev() {
    AnotaTheme(
        dynamicColor = false
    ) {
        HomeScreen()
    }
}