package com.example.messagingapp.domain.crypto

import java.security.PrivateKey
import java.security.PublicKey

interface CryptoRepository {

    fun exportPublicKeyToString(pub: PublicKey): String
    fun importPublicKeyFromString(base64: String): PublicKey

    fun exportPrivateKeyToString(priv: PrivateKey): String
    fun importPrivateKeyFromString(base64: String): PrivateKey
}