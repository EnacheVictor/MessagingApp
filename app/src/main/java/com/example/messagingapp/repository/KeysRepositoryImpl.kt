package com.example.messagingapp.repository

import com.example.messagingapp.model.data.KeysDatabaseDao
import com.example.messagingapp.model.data.KeysEntity

class KeysRepositoryImpl (
    private val dao: KeysDatabaseDao
) : KeysRepository {
    override suspend fun savePrivateKey(base64: String) {
        dao.upsert(KeysEntity(id = 0, privateKey = base64))
    }
    override suspend fun getPrivateKey(): String? = dao.getPrivateKey()
}