package edu.alisson.anota.domain.model

import androidx.compose.ui.graphics.Color

data class Space(
    val id: String,
    val title: String,
    val description: String,
    val color: Color,
    val notes: List<Note>? = null
)
