package edu.alisson.anota.data.mappers

import edu.alisson.anota.data.local.last_note.LastSeenNoteEntity
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

fun LastSeenNoteEntity.toNote(): Note = Note(
    id = id.toString(),
    title = title,
    content = content,
    spaceID = spaceId,
    spaceTitle = spaceTitle,
    createdAt = createdAt
)