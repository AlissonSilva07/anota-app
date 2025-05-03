package edu.alisson.anota.data.local.last_note

import androidx.room.Database
import androidx.room.RoomDatabase
import edu.alisson.anota.data.local.user.UserDao
import edu.alisson.anota.data.local.user.UserEntity

@Database(entities = [LastSeenNoteEntity::class], version = 1)
abstract class LastSeenNoteDatabase : RoomDatabase() {
    abstract fun lastSeenNoteDao(): LastSeenNoteDao
}
