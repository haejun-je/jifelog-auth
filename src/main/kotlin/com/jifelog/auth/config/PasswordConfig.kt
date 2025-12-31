package com.jifelog.auth.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder

@Configuration
class PasswordConfig {
    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return Argon2PasswordEncoder(
            16, // salt length (bytes)
            32, // hash length (bytes)
            1, // parallelism (CPU cores)
            32768, // memory (KB) → 32MB
            3 // iterations
        )
    }
}