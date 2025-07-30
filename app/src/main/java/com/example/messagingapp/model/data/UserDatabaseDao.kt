package com.example.messagingapp.model.data

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface UserDatabaseDao {

    @Query("SELECT * FROM users")
    suspend fun getAllUsers(): List<UserEntity>

    @Upsert
    suspend fun insertUser(user: UserEntity)

    @Query("DELETE FROM users WHERE username = :username")
    suspend fun deleteUser(username: String)

    @Query("UPDATE users SET status = :newStatus WHERE username = :username")
    suspend fun updateStatus(username: String, newStatus: String)

    @Query("SELECT status FROM users WHERE username = :username LIMIT 1")
    suspend fun getStatusForUser(username: String): String

    @Query("SELECT * FROM users WHERE isFavorite = 1")
    suspend fun getFavoriteUsers(): List<UserEntity>

    @Query("UPDATE users SET isFavorite = :isFavorite WHERE username = :username")
    suspend fun setFavorite(username: String, isFavorite: Boolean)

    @Query("DELETE FROM users")
    suspend fun deleteAllUsers()
}