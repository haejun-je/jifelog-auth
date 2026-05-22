package com.jifelog.auth.domain

import com.fasterxml.uuid.Generators
import java.time.Instant
import java.util.UUID

class Credential(
    val id: UUID,
    val userInfoId: UUID,
    val loginId: String,
    val passwordHash: String,
    val passwordAlgo: PasswordAlgoType,
    val passwordUpdatedAt: Instant,
    val failedCount: Int,
    val lockedUntil: Instant? = null,
    val createdAt: Instant,
    val updatedAt: Instant
) {
    companion object {
        fun withoutId(
            userInfoId: UUID,
            loginId: String,
            passwordHash: String,
            passwordAlgo: PasswordAlgoType
        ): Credential = Credential(
            id = Generators.timeBasedEpochGenerator().generate(),
            loginId = loginId,
            userInfoId = userInfoId,
            passwordHash = passwordHash,
            passwordAlgo = passwordAlgo,
            passwordUpdatedAt = Instant.now(),
            failedCount = 0,
            createdAt = Instant.now(),
            updatedAt = Instant.now()
        )

        fun withId(
            id: UUID,
            userInfoId: UUID,
            loginId: String,
            passwordHash: String,
            passwordAlgo: PasswordAlgoType,
            passwordUpdatedAt: Instant,
            failedCount: Int,
            lockedUntil: Instant?,
            createdAt: Instant,
            updatedAt: Instant
        ): Credential = Credential(
            id, userInfoId, loginId, passwordHash,
            passwordAlgo, passwordUpdatedAt, failedCount,
            lockedUntil, createdAt, updatedAt
        )
    }

    fun isLocked(): Boolean = lockedUntil?.isAfter(Instant.now()) == true

    fun resetFailures(): Credential =
        copy(failedCount = 0, lockedUntil = null, updatedAt = Instant.now())

    private fun copy(
        id: UUID = this.id,
        userInfoId: UUID = this.userInfoId,
        loginId: String = this.loginId,
        passwordHash: String = this.passwordHash,
        passwordAlgo: PasswordAlgoType = this.passwordAlgo,
        passwordUpdatedAt: Instant = this.passwordUpdatedAt,
        failedCount: Int = this.failedCount,
        lockedUntil: Instant? = this.lockedUntil,
        createdAt: Instant = this.createdAt,
        updatedAt: Instant = this.updatedAt
    ): Credential = Credential(
        id, userInfoId, loginId, passwordHash,
        passwordAlgo, passwordUpdatedAt, failedCount,
        lockedUntil, createdAt, updatedAt
    )
}
