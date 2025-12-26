package com.jifelog.auth.infra.persistence.mapper

import com.jifelog.auth.domain.User
import com.jifelog.auth.domain.UserStatusType
import com.jifelog.auth.infra.persistence.entity.UserEntity
import com.jifelog.auth.infra.persistence.entity.enums.UserStatus

object UserMapper {
    fun toDomain(entity: UserEntity): User =
        User(
            id = entity.id,
            username = entity.username,
            email = entity.email,
            status = entity.status.toDomain(),
            createdAt = entity.createdAt,
            updatedAt = entity.updatedAt,
            deletedAt = entity.deletedAt
        )

    private fun UserStatus.toDomain(): UserStatusType =
        when (this) {
            UserStatus.ACTIVE -> UserStatusType.ACTIVE
            UserStatus.DISABLED -> UserStatusType.DISABLED
            UserStatus.LOCKED -> UserStatusType.LOCKED
        }
}