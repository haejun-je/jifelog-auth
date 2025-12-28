package com.jifelog.auth.domain

import java.time.OffsetDateTime
import java.util.UUID

class User(
    val id: UUID,
    val username: String,
    val email: String?,
    val status: UserStatusType,
    val createdAt: OffsetDateTime,
    val updatedAt: OffsetDateTime,
    val deletedAt: OffsetDateTime?
) {
    fun isActive(): Boolean =
        status == UserStatusType.ACTIVE && deletedAt == null

    fun deactivate(now: OffsetDateTime): User =
        copy(
            status = UserStatusType.DISABLED,
            updatedAt = now
        )

    private fun copy(
        id: UUID = this.id,
        username: String = this.username,
        email: String? = this.email,
        status: UserStatusType = this.status,
        createdAt: OffsetDateTime = this.createdAt,
        updatedAt: OffsetDateTime = this.updatedAt,
        deletedAt: OffsetDateTime? = this.deletedAt
    ): User =
        User(id, username, email, status, createdAt, updatedAt, deletedAt)
}