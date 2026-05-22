package com.jifelog.auth.infra.persistence.mapper

import com.jifelog.auth.domain.Credential
import com.jifelog.auth.domain.PasswordAlgoType
import com.jifelog.auth.infra.persistence.entity.CredentialEntity
import com.jifelog.auth.infra.persistence.entity.enums.PasswordAlgo
import java.util.UUID

object CredentialMapper {
    fun toDomain(entity: CredentialEntity): Credential =
        Credential.withId(
            id = entity.id,
            userInfoId = entity.userInfoId,
            loginId = entity.loginId,
            passwordHash = entity.passwordHash,
            passwordAlgo = entity.passwordAlgo.toDomain(),
            passwordUpdatedAt = entity.passwordUpdatedAt,
            failedCount = entity.failedCount,
            lockedUntil = entity.lockedUntil,
            createdAt = entity.createdAt,
            updatedAt = entity.updatedAt
        )

    fun toEntity(credential: Credential): CredentialEntity =
        CredentialEntity(
            id = UUID.randomUUID(),
            userInfoId = credential.userInfoId,
            loginId = credential.loginId,
            passwordHash = credential.passwordHash,
            passwordAlgo = credential.passwordAlgo.toEntity(),
            passwordUpdatedAt = credential.passwordUpdatedAt,
            failedCount = credential.failedCount,
            lockedUntil = credential.lockedUntil,
            createdAt = credential.createdAt,
            updatedAt = credential.updatedAt
        )

    private fun PasswordAlgo.toDomain(): PasswordAlgoType =
        when (this) {
            PasswordAlgo.ARGON2ID -> PasswordAlgoType.ARGON2ID
        }

    private fun PasswordAlgoType.toEntity(): PasswordAlgo =
        when (this) {
            PasswordAlgoType.ARGON2ID -> PasswordAlgo.ARGON2ID
        }
}
