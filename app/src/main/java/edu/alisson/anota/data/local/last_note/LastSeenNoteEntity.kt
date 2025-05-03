package edu.alisson.anota.data.local.last_note

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "last_seen_note")
data class LastSeenNoteEntity(
    @PrimaryKey val id: String,
    val title: String,
    val content: String,
    val spaceTitle: String,
    val spaceId: String,
    val createdAt: String
)

