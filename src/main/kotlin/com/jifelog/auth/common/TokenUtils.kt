package com.jifelog.auth.common

import java.security.SecureRandom

object TokenUtils {
    private const val CHAR_POOL = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"

    private val random = SecureRandom()

    fun generateToken(length: Int = 16): String {
        require(length > 0) { "length must be positive" }

        return buildString(length) {
            repeat(length) {
                val index = random.nextInt(CHAR_POOL.length)
                append(CHAR_POOL[index])
            }
        }
    }
}