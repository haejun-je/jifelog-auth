package com.jifelog.auth.application

import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component

@Component
class PasswordHasher(
    private val passwordEncoder: PasswordEncoder
) {
    fun encode(password: String): String {
        return passwordEncoder.encode(password)!!
    }
}