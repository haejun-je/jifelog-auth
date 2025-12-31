package com.jifelog.auth.domain

import java.time.Instant
import java.util.UUID

class UserPassword(
    val userId: UUID? = null,
    val passwordHash: String,
    val algorithm: PasswordAlgoType,
    val failedCount: Int,
    val lockedUntil: Instant? = null,
    val passwordUpdatedAt: Instant,
    val updatedAt: Instant
) {
   companion object {
       fun withoutId(
           passwordHash: String,
           algorithm: PasswordAlgoType
       ): UserPassword {
           return UserPassword(
               passwordHash = passwordHash,
               algorithm = algorithm,
               failedCount = 0,
               passwordUpdatedAt = Instant.now(),
               updatedAt = Instant.now()
           )
       }

       fun withId(
           userId: UUID,
           passwordHash: String,
           algorithm: PasswordAlgoType,
           failedCount: Int,
           lockedUntil: Instant?,
           passwordUpdatedAt: Instant,
           updatedAt: Instant
       ): UserPassword {
           return UserPassword(
               userId,
               passwordHash,
               algorithm,
               failedCount,
               lockedUntil,
               passwordUpdatedAt,
               updatedAt
           )
       }
   }

    fun isLocked(): Boolean =
        lockedUntil?.isAfter(Instant.now()) == true

    fun resetFailures(): UserPassword =
        copy(
            failedCount = 0,
            lockedUntil = null,
            updatedAt = Instant.now()
        )

    private fun copy(
        userId: UUID = this.userId!!,
        passwordHash: String = this.passwordHash,
        algorithm: PasswordAlgoType = this.algorithm,
        failedCount: Int = this.failedCount,
        lockedUntil: Instant? = this.lockedUntil,
        passwordUpdatedAt: Instant = this.passwordUpdatedAt,
        updatedAt: Instant = this.updatedAt
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