package com.jifelog.auth.infra.persistence.mapper

import com.jifelog.auth.domain.PasswordAlgoType
import com.jifelog.auth.domain.User
import com.jifelog.auth.domain.UserPassword
import com.jifelog.auth.domain.UserStatusType
import com.jifelog.auth.infra.persistence.entity.UserEntity
import com.jifelog.auth.infra.persistence.entity.UserPasswordEntity
import com.jifelog.auth.infra.persistence.entity.enums.PasswordAlgo
import com.jifelog.auth.infra.persistence.entity.enums.UserStatus

object UserMapper {
    fun toDomain(
        userEntity: UserEntity,
        userPasswordEntity: UserPasswordEntity
    ): User =
        User.withId(
            id = userEntity.id!!,
            username = userEntity.username,
            email = userEntity.email,
            status = userEntity.status.toDomain(),
            createdAt = userEntity.createdAt,
            updatedAt = userEntity.updatedAt,
            deletedAt = userEntity.deletedAt,
            userPassword = UserPassword.withId(
                userPasswordEntity.userId,
                userPasswordEntity.passwordHash,
                userPasswordEntity.passwordAlgo.toDomain(),
                userPasswordEntity.failedCount,
                userPasswordEntity.lockedUntil,
                userPasswordEntity.passwordUpdatedAt,
                userPasswordEntity.updatedAt
            )
        )

    fun toEntity(user: User): UserEntity {
        return UserEntity(
            user.id,
            user.username,
            user.email,
            user.status.toEntity(),
            user.createdAt,
            user.updatedAt,
            user.deletedAt
        )
    }

    private fun UserStatus.toDomain(): UserStatusType =
        when (this) {
            UserStatus.ACTIVE -> UserStatusType.ACTIVE
            UserStatus.DISABLED -> UserStatusType.DISABLED
            UserStatus.LOCKED -> UserStatusType.LOCKED
        }

    private fun UserStatusType.toEntity(): UserStatus =
        when (this) {
            UserStatusType.ACTIVE -> UserStatus.ACTIVE
            UserStatusType.DISABLED -> UserStatus.DISABLED
            UserStatusType.LOCKED -> UserStatus.LOCKED
        }

    private fun PasswordAlgo.toDomain(): PasswordAlgoType =
        when (this) {
            PasswordAlgo.ARGON2ID -> PasswordAlgoType.ARGON2ID
        }
}