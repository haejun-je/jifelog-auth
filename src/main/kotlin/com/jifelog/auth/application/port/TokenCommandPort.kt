package com.jifelog.auth.application.port

interface TokenCommandPort {
    fun saveEmailVerificationToken(
        email: String,
        hashedToken: String,
        ttlSeconds: Long
    )

    fun saveEmailVerified(
        email: String,
        ttlSeconds: Long
    )
}