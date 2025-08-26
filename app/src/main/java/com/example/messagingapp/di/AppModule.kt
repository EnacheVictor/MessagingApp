package com.example.messagingapp.di

import android.app.Application
import androidx.room.Room
import com.example.messagingapp.domain.crypto.CryptoRepository
import com.example.messagingapp.domain.crypto.EncryptorAndDecryptor
import com.example.messagingapp.domain.crypto.JcaCryptoRepository
import com.example.messagingapp.model.data.KeysDatabaseDao
import com.example.messagingapp.model.data.MessageDatabaseDao
import com.example.messagingapp.model.data.UserDatabase
import com.example.messagingapp.model.data.UserDatabaseDao
import com.example.messagingapp.model.network.ApiService
import com.example.messagingapp.repository.KeysRepository
import com.example.messagingapp.repository.KeysRepositoryImpl
import com.example.messagingapp.repository.MessageRepository
import com.example.messagingapp.repository.MessageRepositoryImpl
import com.example.messagingapp.repository.UserRepositoryImpl
import com.example.messagingapp.repository.UserRepository
import dagger.Module
import dagger.Provides
import retrofit2.converter.gson.GsonConverterFactory
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(app: Application): UserDatabase =
        Room.databaseBuilder(app, UserDatabase::class.java, "messaging_db").fallbackToDestructiveMigration().build()

    @Provides
    fun provideUserDao(db: UserDatabase): UserDatabaseDao = db.userDao()

    @Provides
    fun provideMessageDao(db: UserDatabase): MessageDatabaseDao = db.messageDao()

    @Provides
    fun provideUserRepository(
        userDao: UserDatabaseDao,
        apiService: ApiService
    ): UserRepository = UserRepositoryImpl(userDao, apiService)

    @Provides
    @Singleton
    fun provideMessageEncryptor(
        userRepo: UserRepository,
        keysRepo: KeysRepository,
        crypto: CryptoRepository
    ): EncryptorAndDecryptor = EncryptorAndDecryptor(userRepo, keysRepo, crypto)

    @Provides
    fun provideMessageRepository(
        messageDao: MessageDatabaseDao,
        userRepo: UserRepository,
        keysRepo: KeysRepository,
        crypto: CryptoRepository
    ): MessageRepository =
        MessageRepositoryImpl(messageDao, userRepo, keysRepo, crypto)

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit =
        Retrofit.Builder()
            .baseUrl("http://10.0.2.2:5263/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService =
        retrofit.create(ApiService::class.java)

    @Provides
    @Singleton
    fun provideCryptoRepository(): CryptoRepository = JcaCryptoRepository()

    @Provides
    fun providePrivateKeyDao(db: UserDatabase): KeysDatabaseDao = db.keyDao()

    @Provides
    fun provideKeysRepository(dao: KeysDatabaseDao): KeysRepository =
        KeysRepositoryImpl(dao)
}
