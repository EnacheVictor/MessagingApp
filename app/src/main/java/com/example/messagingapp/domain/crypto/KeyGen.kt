package com.example.messagingapp.domain.crypto

import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.SecureRandom

object KeyGen {
    fun generateKeyPair(): KeyPair{
        val key = KeyPairGenerator.getInstance("DH")
        val rng = SecureRandom()
        key.initialize(2048,rng)
        return key.generateKeyPair()
    }
}