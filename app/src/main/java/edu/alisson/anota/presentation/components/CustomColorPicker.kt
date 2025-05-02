package edu.alisson.anota.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun CustomColorPicker(
    initialColor: Color = Color.Blue,
    onColorSelected: (Color) -> Unit
) {
    var color by remember { mutableStateOf(initialColor) }

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Cor:",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.primary,
            )
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
                    .background(color, shape = RoundedCornerShape(8.dp))
            )
        }

        Slider(
            value = color.red,
            onValueChange = {
                val updatedColor = color.copy(red = it)
                color = updatedColor
                onColorSelected(updatedColor)
            },
            valueRange = 0f..1f,
            colors = SliderDefaults.colors(thumbColor = Color.Red)
        )

        Slider(
            value = color.green,
            onValueChange = {
                val updatedColor = color.copy(green = it)
                color = updatedColor
                onColorSelected(updatedColor)
            },
            valueRange = 0f..1f,
            colors = SliderDefaults.colors(thumbColor = Color.Green)
        )

        Slider(
            value = color.blue,
            onValueChange = {
                val updatedColor = color.copy(blue = it)
                color = updatedColor
                onColorSelected(updatedColor)
            },
            valueRange = 0f..1f,
            colors = SliderDefaults.colors(thumbColor = Color.Blue)
        )
    }
}