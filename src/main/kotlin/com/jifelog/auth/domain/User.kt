package com.jifelog.auth.domain

import com.fasterxml.uuid.Generators
import java.time.Instant
import java.util.UUID

class User(
    val id: UUID,
    val nickname: String,
    val username: String,
    val profileImg: String,
    val createdAt: Instant,
    val updatedAt: Instant,
) {
    companion object {
        fun withoutId(
            name: String,
            nickname: String,
            profileImg: String
        ): User {
            return User(
                id = Generators.timeBasedEpochGenerator().generate(),
                username = name,
                nickname = nickname,
                profileImg = profileImg,
                createdAt = Instant.now(),
                updatedAt = Instant.now()
            )
        }

        fun withId(
            id: UUID,
            username: String,
            nickname: String,
            profileImg: String,
            createdAt: Instant,
            updatedAt: Instant,
        ): User = User(
            id = id,
            username = username,
            nickname = nickname,
            profileImg = profileImg,
            createdAt = createdAt,
            updatedAt = updatedAt
        )
    }
}
