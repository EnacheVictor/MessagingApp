package com.example.messagingapp.repository

import android.util.Log
import com.example.messagingapp.model.data.UserDatabaseDao
import com.example.messagingapp.model.data.UserEntity
import com.example.messagingapp.model.network.ApiService
import com.example.messagingapp.model.network.LoginDto
import com.example.messagingapp.model.network.SignUpDto
import com.example.messagingapp.model.network.toEntity

class UserRepositoryImpl(private val dao: UserDatabaseDao,
                         private val apiService: ApiService) : UserRepository {

    override suspend fun getAllUsers(): List<UserEntity> {
        return dao.getAllUsers()
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


    override suspend fun setFavorite(username: String, isFavorite: Boolean) {
        dao.setFavorite(username, isFavorite)
    }

    override suspend fun signUp(username: String, password: String, publicKey: String): Boolean {
        val dto = SignUpDto(username, password, publicKey)
        Log.d("UserRepository", "SignUpDto being sent: $dto")
        val response = apiService.signUp(dto)
        Log.d("UserRepository", "SignUp response: success=${response.isSuccessful}, code=${response.code()}")
        return response.isSuccessful
    }

    override suspend fun login(username: String,password: String): Boolean{
        val dto = LoginDto(username, password)
        val response = apiService.login(dto)
        return response.isSuccessful
    }

    override suspend fun usersFromServer() {
        val response = apiService.getAllUsers()
        if (response.isSuccessful) {
            val users = response.body()?.map { it.toEntity() } ?: emptyList()
            Log.d("Crypto", "Fetched users from server: $users")
            dao.deleteAllUsers()
            if (users.isNotEmpty()) {
                dao.insertUsers(users)
            }
        }
    }
}