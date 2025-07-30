package com.example.messagingapp.repository

import com.example.messagingapp.model.data.UserEntity

interface UserRepository {
    suspend fun getAllUsers(): List<UserEntity>
    suspend fun insertUser(username: String)
    suspend fun deleteUser(username: String)
    suspend fun updateStatus(username: String, newStatus: String)
    suspend fun getStatusForUser(username: String): String
    suspend fun getFavorites(): List<UserEntity>
    suspend fun addToFavorites(username: String)
    suspend fun removeFromFavorites(username: String)
    suspend fun setFavorite(username: String, isFavorite: Boolean)
    suspend fun signUp(username: String, password: String): Boolean
    suspend fun login(username: String,password: String): Boolean
    suspend fun usersFromServer()
}