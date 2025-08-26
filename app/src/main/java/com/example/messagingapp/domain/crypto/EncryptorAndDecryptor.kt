package com.example.messagingapp.domain.crypto

import android.util.Base64
import android.util.Log
import com.example.messagingapp.repository.KeysRepository
import com.example.messagingapp.repository.UserRepository
import java.security.MessageDigest
import javax.inject.Inject
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

    class EncryptorAndDecryptor @Inject constructor(
        private val userRepo: UserRepository,
        private val keysRepo: KeysRepository,
        private val crypto: CryptoRepository
    ) {

        suspend fun encryptFor(receiverUsername: String, plaintext: String): String? {
            val privB64 = keysRepo.getPrivateKey()
            if (privB64 == null) {
                Log.e("Crypto", "Private key missing. User must SignUp on this device first.")
                return null
            }
            val myPrivate = crypto.importPrivateKeyFromString(privB64)

            val receiverPubB64 = userRepo.getPublicKeyForUser(receiverUsername)
            if (receiverPubB64.isNullOrBlank()) {
                Log.e("Crypto", "Receiver public key missing for $receiverUsername")
                return null
            }
            val receiverPublic = crypto.importPublicKeyFromString(receiverPubB64)

            val dh = KeyExchange(myPrivate, receiverPublic)
            dh.initDHKeyAgreement()
            val shared = dh.getSecret()

            val aesKeyBytes = sha256(shared)

            return encryptEcbBase64(plaintext, aesKeyBytes)
        }

        private fun sha256(bytes: ByteArray): ByteArray {
            val md = MessageDigest.getInstance("SHA-256")
            return md.digest(bytes)
        }

        private fun encryptEcbBase64(plainText: String, secretKey: ByteArray): String {
            val key = SecretKeySpec(secretKey, "AES")
            val cipher = Cipher.getInstance("AES/ECB/PKCS5Padding")
            cipher.init(Cipher.ENCRYPT_MODE, key)
            val ct = cipher.doFinal(plainText.toByteArray(Charsets.UTF_8))
            return Base64.encodeToString(ct, Base64.NO_WRAP)
        }

        suspend fun decryptFrom(senderUsername: String, ciphertextB64: String): String? {
            val privB64 = keysRepo.getPrivateKey()
            if (privB64 == null) {
                Log.e("Crypto", "Private key missing. User must SignUp on this device first.")
                return null
            }
            val myPrivate = crypto.importPrivateKeyFromString(privB64)

            val senderPubB64 = userRepo.getPublicKeyForUser(senderUsername)
            if (senderPubB64.isNullOrBlank()) {
                Log.e("Crypto", "Sender public key missing for $senderUsername")
                return null
            }
            val senderPublic = crypto.importPublicKeyFromString(senderPubB64)

            val dh = KeyExchange(myPrivate, senderPublic)
            dh.initDHKeyAgreement()
            val shared = dh.getSecret()

            val aesKeyBytes = sha256(shared)

            return decryptEcbBase64(ciphertextB64, aesKeyBytes)
        }

        private fun decryptEcbBase64(cipherTextB64: String, secretKey: ByteArray): String {
            val key = SecretKeySpec(secretKey, "AES")
            val cipher = Cipher.getInstance("AES/ECB/PKCS5Padding")
            cipher.init(Cipher.DECRYPT_MODE, key)
            val decoded = Base64.decode(cipherTextB64, Base64.NO_WRAP)
            val pt = cipher.doFinal(decoded)
            return String(pt, Charsets.UTF_8)
        }

    }