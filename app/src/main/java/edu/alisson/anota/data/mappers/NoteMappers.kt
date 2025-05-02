package edu.alisson.anota.data.mappers

import edu.alisson.anota.domain.model.Note

fun Note.toMap(): Map<String, Any> = mapOf(
    "id" to id,
    "title" to title,
    "content" to content,
    "spaceID" to spaceID,
    "spaceTitle" to spaceTitle,
    "createdAt" to createdAt,
    "updatedAt" to updatedAt
)