package com.example.messagingapp.domain.crypto

import java.security.PublicKey

interface CryptoRepository {

    fun exportPublicKeyToString(pub: PublicKey): String
    fun importPublicKeyFromString(base64: String): PublicKey
}