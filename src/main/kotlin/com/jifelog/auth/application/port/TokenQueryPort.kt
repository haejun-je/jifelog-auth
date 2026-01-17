package com.jifelog.auth.application.port

interface TokenQueryPort {
    fun loadEmailVerificationToken(email: String): String
    fun checkEmailVerified(email: String): Boolean
}