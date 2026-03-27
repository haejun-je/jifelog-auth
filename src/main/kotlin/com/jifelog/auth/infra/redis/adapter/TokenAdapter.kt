package com.jifelog.auth.infra.redis.adapter

import com.jifelog.auth.application.port.TokenCommandPort
import com.jifelog.auth.application.port.TokenQueryPort
import com.jifelog.auth.common.HashUtils
import com.jifelog.auth.common.exception.AuthException
import com.jifelog.auth.common.exception.ErrorCode
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Component
import java.time.Duration

@Component
class TokenAdapter(
    private val stringRedisTemplate: StringRedisTemplate
) : TokenCommandPort, TokenQueryPort {
    override fun saveEmailVerificationToken(
        email: String,
        hashedToken: String,
        ttlSeconds: Long
    ) {
        val hashedEmail = HashUtils.sha256Base64Url(email)

        stringRedisTemplate.opsForValue()
            .set(
                "auth:signup:email-verify:$hashedEmail",
                hashedToken,
                Duration.ofSeconds(ttlSeconds)
            )
    }

    override fun loadEmailVerificationToken(email: String): String {
        val hashedEmail = HashUtils.sha256Base64Url(email)
        val key = "auth:signup:email-verify:$hashedEmail"

        return stringRedisTemplate.opsForValue()
            .get(key)
            ?: throw AuthException(ErrorCode.U_01_002)
    }

    override fun checkEmailVerified(email: String): Boolean {
        val hashedEmail = HashUtils.sha256Base64Url(email)
        return stringRedisTemplate.hasKey("auth:signup:email-verified:$hashedEmail")
    }

    override fun saveEmailVerified(email: String, ttlSeconds: Long) {
        val hashedEmail = HashUtils.sha256Base64Url(email)

        stringRedisTemplate.opsForValue()
            .set(
                "auth:signup:email-verified:$hashedEmail",
                "1",
                Duration.ofSeconds(ttlSeconds)
            )
    }
}