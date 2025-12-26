package com.jifelog.auth.infra.persistence.repository

import com.jifelog.auth.infra.persistence.entity.UserEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface UserJpaRepository : JpaRepository<UserEntity, UUID> {
}