package com.example.messagingapp.domain.crypto

import android.util.Base64
import java.security.KeyFactory
import java.security.PrivateKey
import java.security.PublicKey
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec

class JcaCryptoRepository : CryptoRepository {
    override fun exportPublicKeyToString(pub: PublicKey): String {
      return Base64.encodeToString(pub.encoded, Base64.NO_WRAP)
    }

    override fun importPublicKeyFromString(base64: String): PublicKey {
        val bytes = Base64.decode(base64, Base64.DEFAULT)
        val spec = X509EncodedKeySpec(bytes)
        val key = KeyFactory.getInstance("DH")
        return key.generatePublic(spec)
    }

    override fun exportPrivateKeyToString(priv: PrivateKey): String =
        Base64.encodeToString(priv.encoded, Base64.NO_WRAP)

    override fun importPrivateKeyFromString(base64: String): PrivateKey {
        val bytes = Base64.decode(base64, Base64.DEFAULT)
        val spec = PKCS8EncodedKeySpec(bytes)
        return KeyFactory.getInstance("DH").generatePrivate(spec)
    }
}