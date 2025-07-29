package com.example.messagingapp.repository

import com.example.messagingapp.model.data.UserDatabaseDao
import com.example.messagingapp.model.data.UserEntity

class UserRepositoryImpl(private val dao: UserDatabaseDao) : UserRepository {

    override suspend fun getAllUsers(): List<UserEntity> {
        return dao.getAllUsers()
    }

    override suspend fun insertUser(username: String) {
        dao.insertUser(UserEntity(username))
    }

    override suspend fun populateInitialUsers() {
        val users = listOf(
            "Maria Popescu",
            "Andrei Ionescu",
            "Alexandra Marin",
            "Ion Pop",
            "Cristina Dinu"
        )
        users.forEach { dao.insertUser(UserEntity(it)) }
    }

    override suspend fun deleteUser(username: String) {
        dao.deleteUser(username)
    }

    override suspend fun updateStatus(username: String, newStatus: String) {
        dao.updateStatus(username, newStatus)
    }

    override suspend fun getStatusForUser(username: String): String {
        return dao.getStatusForUser(username)
    }

    override suspend fun getFavorites(): List<UserEntity> {
        return dao.getFavoriteUsers()
    }

    override suspend fun addToFavorites(username: String) {
        dao.setFavorite(username, true)
    }

    override suspend fun removeFromFavorites(username: String) {
        dao.setFavorite(username, false)
    }
    override suspend fun setFavorite(username: String, isFavorite: Boolean) {
        dao.setFavorite(username, isFavorite)
    }
}