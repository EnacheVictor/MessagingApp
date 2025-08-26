package com.example.messagingapp.model.data

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface KeysDatabaseDao {
    @Upsert
    suspend fun upsert(entity: KeysEntity)

    @Query("SELECT privateKey FROM key_table WHERE id = 0 LIMIT 1")
    suspend fun getPrivateKey(): String?
}