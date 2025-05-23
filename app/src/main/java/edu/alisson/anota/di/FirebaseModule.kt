package edu.alisson.anota.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    @Provides
    @Singleton
    fun provideFirebaseDatabase(): FirebaseDatabase {
        val database = FirebaseDatabase.getInstance()
        database.setLogLevel(com.google.firebase.database.Logger.Level.DEBUG)
        return database
    }

    @Provides
    @Singleton
    fun provideDatabaseReference(firebaseDatabase: FirebaseDatabase): DatabaseReference {
        return firebaseDatabase.reference
    }
}