package com.jifelog.auth.domain

import java.time.OffsetDateTime
import java.util.UUID

class UserPassword(
    val userId: UUID,
    val passwordHash: String,
    val algorithm: PasswordAlgoType,
    val failedCount: Int,
    val lockedUntil: OffsetDateTime?,
    val passwordUpdatedAt: OffsetDateTime,
    val updatedAt: OffsetDateTime
) {
    fun isLocked(now: OffsetDateTime): Boolean =
        lockedUntil?.isAfter(now) == true

    fun resetFailures(now: OffsetDateTime): UserPassword =
        copy(
            failedCount = 0,
            lockedUntil = null,
            updatedAt = now
        )

    private fun copy(
        userId: UUID = this.userId,
        passwordHash: String = this.passwordHash,
        algorithm: PasswordAlgoType = this.algorithm,
        failedCount: Int = this.failedCount,
        lockedUntil: OffsetDateTime? = this.lockedUntil,
        passwordUpdatedAt: OffsetDateTime = this.passwordUpdatedAt,
        updatedAt: OffsetDateTime = this.updatedAt
    ): UserPassword =
        UserPassword(
            userId,
            passwordHash,
            algorithm,
            failedCount,
            lockedUntil,
            passwordUpdatedAt,
            updatedAt
        )
}