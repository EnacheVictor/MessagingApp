package com.example.messagingapp.repository

interface KeysRepository {
    suspend fun savePrivateKey(base64: String)
    suspend fun getPrivateKey(): String?
}