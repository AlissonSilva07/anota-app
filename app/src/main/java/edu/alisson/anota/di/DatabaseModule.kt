package edu.alisson.anota.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import edu.alisson.anota.data.local.last_note.LastSeenNoteDao
import edu.alisson.anota.data.local.last_note.LastSeenNoteDatabase
import edu.alisson.anota.data.local.user.UserDao
import edu.alisson.anota.data.local.user.UserDatabase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Provides
    @Singleton
    fun provideUserDatabase(@ApplicationContext context: Context): UserDatabase {
        return Room.databaseBuilder(
            context,
            UserDatabase::class.java,
            "user_database"
        ).build()
    }

    @Provides
    fun provideUserDao(database: UserDatabase): UserDao {
        return database.userDao()
    }

    @Provides
    @Singleton
    fun provideLastSeenNoteDatabase(@ApplicationContext context: Context): LastSeenNoteDatabase {
        return Room.databaseBuilder(
            context,
            LastSeenNoteDatabase::class.java,
            "last_seen_note_database"
        ).build()
    }

    @Provides
    fun provideLastSeenNoteDao(database: LastSeenNoteDatabase): LastSeenNoteDao {
        return database.lastSeenNoteDao()
    }
}