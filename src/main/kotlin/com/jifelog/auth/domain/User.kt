package com.jifelog.auth.domain

import java.time.Instant
import java.util.UUID

class User(
    val id: UUID? = null,
    val username: String,
    val email: String,
    val status: UserStatusType,
    val createdAt: Instant,
    val updatedAt: Instant,
    val deletedAt: Instant? = null,
    val userPassword: UserPassword
) {
    companion object {
        fun withoutId(
            username: String,
            email: String,
            userPassword: UserPassword
        ): User {
            return User(
                username = username,
                email = email,
                status = UserStatusType.ACTIVE,
                createdAt = Instant.now(),
                updatedAt = Instant.now(),
                userPassword = userPassword,
            )
        }

        fun withId(
            id: UUID,
            username: String,
            email: String,
            status: UserStatusType,
            createdAt: Instant,
            updatedAt: Instant,
            deletedAt: Instant?,
            userPassword: UserPassword
        ): User {
            return User(
                id,
                username,
                email,
                status,
                createdAt,
                updatedAt,
                deletedAt,
                userPassword
            )
        }
    }

    fun isActive(): Boolean =
        status == UserStatusType.ACTIVE && deletedAt == null

    fun deactivate(): User =
        copy(
            status = UserStatusType.DISABLED,
            updatedAt = Instant.now()
        )

    private fun copy(
        id: UUID = this.id!!,
        username: String = this.username,
        email: String = this.email,
        status: UserStatusType = this.status,
        createdAt: Instant = this.createdAt,
        updatedAt: Instant = this.updatedAt,
        deletedAt: Instant? = this.deletedAt,
        userPassword: UserPassword = this.userPassword
    ): User =
        User(id, username, email, status, createdAt, updatedAt, deletedAt, userPassword)
}