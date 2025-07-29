package com.example.messagingapp.di

import android.app.Application
import androidx.room.Room
import com.example.messagingapp.model.data.MessageDatabaseDao
import com.example.messagingapp.model.data.UserDatabase
import com.example.messagingapp.model.data.UserDatabaseDao
import com.example.messagingapp.repository.MessageRepository
import com.example.messagingapp.repository.MessageRepositoryImpl
import com.example.messagingapp.repository.UserRepositoryImpl
import com.example.messagingapp.repository.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
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
    fun provideUserRepository(userDao: UserDatabaseDao): UserRepository =
        UserRepositoryImpl(userDao)

    @Provides
    fun provideMessageRepository(messageDao: MessageDatabaseDao): MessageRepository =
        MessageRepositoryImpl(messageDao)
}
