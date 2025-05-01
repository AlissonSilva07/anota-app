package edu.alisson.anota.data

import androidx.compose.ui.graphics.Color
import edu.alisson.anota.domain.model.Space

object Mocks {
    val spaces = listOf(
        Space(
            id = "1",
            title = "Space 1",
            color = Color(0xFFFF7676),
            description = "New space",
        ),
        Space(
            id = "2",
            title = "Space 2",
            color = Color(0xFF29B21C),
            description = "New space",
        )
    )
}