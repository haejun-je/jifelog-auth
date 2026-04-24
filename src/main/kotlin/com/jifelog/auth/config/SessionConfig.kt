package com.jifelog.auth.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.serializer.JacksonJsonRedisSerializer
import org.springframework.data.redis.serializer.RedisSerializer
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisIndexedHttpSession
import tools.jackson.databind.json.JsonMapper


@Configuration
@EnableRedisIndexedHttpSession(
    maxInactiveIntervalInSeconds = 60 * 30,
    redisNamespace = "jifelog:auth"
)
class SessionConfig {

    @Bean
    fun springSessionDefaultRedisSerializer(jsonMapper: JsonMapper): RedisSerializer<Any> {
        return JacksonJsonRedisSerializer(jsonMapper, Any::class.java)
    }
}
