package com.example.messagingapp.domain.crypto

import java.security.PrivateKey
import java.security.PublicKey
import javax.crypto.KeyAgreement

class KeyExchange (
    privateKeyReceiver: PrivateKey,
    publicKeySender: PublicKey
) {
    private var publicKey: PublicKey = publicKeySender
    private var privateKey: PrivateKey = privateKeyReceiver
    private var keyAgreement: KeyAgreement? = null

    fun initDHKeyAgreement() {
        keyAgreement = KeyAgreement.getInstance("DH")
        keyAgreement?.init(privateKey)
    }

    fun doPhase(publicKey: PublicKey) {
        keyAgreement!!.doPhase(publicKey, true)
    }

    fun getSecret(): ByteArray {
        doPhase(publicKey)
        return keyAgreement!!.generateSecret()
    }
}