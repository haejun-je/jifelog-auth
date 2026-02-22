package com.jifelog.auth.infra.persistence.repository

import com.jifelog.auth.infra.persistence.entity.UserPasswordEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface UserPasswordJpaRepository : JpaRepository<UserPasswordEntity, UUID> {
    fun findByUserId(userId: UUID): UserPasswordEntity?
}