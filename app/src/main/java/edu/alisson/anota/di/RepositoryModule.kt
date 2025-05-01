package edu.alisson.anota.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import edu.alisson.anota.data.repository.AuthRepositoryImpl
import edu.alisson.anota.domain.repository.AuthRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideAuthRepository(
        firebaseAuth: FirebaseAuth,
        database: FirebaseDatabase,
    ): AuthRepository {
        return AuthRepositoryImpl(firebaseAuth, database)
    }
}