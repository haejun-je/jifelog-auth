package com.jifelog.auth.infra.persistence.mapper

import com.jifelog.auth.domain.PasswordAlgoType
import com.jifelog.auth.domain.UserPassword
import com.jifelog.auth.infra.persistence.entity.UserPasswordEntity
import com.jifelog.auth.infra.persistence.entity.enums.PasswordAlgo
import java.util.*

object UserPasswordMapper {
    fun toEntity(
        userId: UUID,
        userPassword: UserPassword
    ): UserPasswordEntity {
        return UserPasswordEntity(
            userId,
            userPassword.passwordHash,
            userPassword.algorithm.toEntity(),
            userPassword.passwordUpdatedAt,
            userPassword.failedCount,
            userPassword.lockedUntil,
            userPassword.updatedAt
        )
    }

    private fun PasswordAlgoType.toEntity(): PasswordAlgo =
        when (this) {
            PasswordAlgoType.ARGON2ID -> PasswordAlgo.ARGON2ID
        }
}