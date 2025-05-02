package edu.alisson.anota.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import edu.alisson.anota.data.local.UserDatabase
import edu.alisson.anota.data.repository.AuthRepositoryImpl
import edu.alisson.anota.data.repository.SpaceRepositoryImpl
import edu.alisson.anota.data.repository.UserRepositoryImpl
import edu.alisson.anota.data.utils.DataStoreManager
import edu.alisson.anota.domain.repository.AuthRepository
import edu.alisson.anota.domain.repository.SpaceRepository
import edu.alisson.anota.domain.repository.UserRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideAuthRepository(
        firebaseAuth: FirebaseAuth,
        database: FirebaseDatabase,
        dataStoreManager: DataStoreManager
    ): AuthRepository {
        return AuthRepositoryImpl(firebaseAuth, database, dataStoreManager)
    }

    @Provides
    @Singleton
    fun provideUserRepository(
        database: FirebaseDatabase,
        localDatabase: UserDatabase
    ): UserRepository {
        return UserRepositoryImpl(database, localDatabase)
    }

    @Provides
    @Singleton
    fun provideSpaceRepository(
        firebaseAuth: FirebaseAuth,
        database: FirebaseDatabase,
    ): SpaceRepository {
        return SpaceRepositoryImpl(firebaseAuth, database)
    }
}