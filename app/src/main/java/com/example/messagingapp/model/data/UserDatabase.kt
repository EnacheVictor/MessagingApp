package com.example.messagingapp.model.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [UserEntity::class, MessageEntity::class, KeysEntity::class], version = 18, exportSchema = false)
abstract class UserDatabase : RoomDatabase() {
    abstract fun userDao(): UserDatabaseDao
    abstract fun messageDao(): MessageDatabaseDao
    abstract fun keyDao(): KeysDatabaseDao
}