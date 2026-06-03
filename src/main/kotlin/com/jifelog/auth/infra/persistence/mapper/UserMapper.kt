package com.jifelog.auth.infra.persistence.mapper

import com.jifelog.auth.domain.User
import com.jifelog.auth.infra.persistence.entity.UserEntity


object UserMapper {
    fun toEntity(user: User): UserEntity = UserEntity(
        id = user.id,
        username = user.username,
        nickname = user.nickname,
        profileImg = user.profileImg,
        createdAt = user.createdAt,
        updatedAt = user.updatedAt
    )

    fun toDomain(entity: UserEntity): User = User.withId(
        id = entity.id,
        name = entity.username,
        nickname = entity.nickname,
        profileImg = entity.profileImg,
        createdAt = entity.createdAt,
        updatedAt = entity.updatedAt,
    )
}
