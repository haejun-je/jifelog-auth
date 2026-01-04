package com.jifelog.auth.infra.redis.adapter

import com.jifelog.auth.application.port.TokenCommandPort
import com.jifelog.auth.common.HashUtils
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Component
import java.time.Duration

@Component
class TokenAdapter(
    private val stringRedisTemplate: StringRedisTemplate
) : TokenCommandPort {
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
}