package edu.alisson.anota.domain.model

data class Note(
    val id: String,
    val title: String,
    val content: String,
    val spaceID: String,
    val spaceTitle: String,
    val createdAt: String,
    val updatedAt: String
)