package com.example.messagingapp.domain.crypto.usecases

import com.example.messagingapp.domain.crypto.CryptoRepository
import java.security.PublicKey

class ImportPublicKeyFromStringUseCase(private val repo: CryptoRepository) {
    operator fun invoke(base64: String): PublicKey = repo.importPublicKeyFromString(base64)
}