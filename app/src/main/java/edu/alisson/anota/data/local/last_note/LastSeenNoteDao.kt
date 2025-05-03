package edu.alisson.anota.data.local.last_note

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface LastSeenNoteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveLastSeenNote(note: LastSeenNoteEntity)

    @Query("SELECT * FROM last_seen_note LIMIT 1")
    suspend fun getLastSeenNote(): LastSeenNoteEntity?

    @Query("DELETE FROM last_seen_note")
    suspend fun clearLastSeenNote()
}