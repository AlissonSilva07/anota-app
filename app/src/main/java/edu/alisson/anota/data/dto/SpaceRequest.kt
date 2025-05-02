package edu.alisson.anota.data.dto

import edu.alisson.anota.domain.model.Note

data class SpaceRequestResponse(
    val id: String,
    val title: String,
    val description: String,
    val color: String,
    val notes: List<Note>? = null,
    val createdAt: String,
    val updatedAt: String? = null,
)
