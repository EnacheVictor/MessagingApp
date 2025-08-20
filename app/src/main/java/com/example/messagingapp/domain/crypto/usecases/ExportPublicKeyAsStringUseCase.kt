package com.example.messagingapp.domain.crypto.usecases

import com.example.messagingapp.domain.crypto.CryptoRepository
import java.security.PublicKey

class ExportPublicKeyAsStringUseCase(private val repo: CryptoRepository) {
    operator fun invoke(pub: PublicKey): String = repo.exportPublicKeyToString(pub)
}