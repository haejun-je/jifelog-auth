package com.jifelog.auth.infra.persistence.repository

import com.jifelog.auth.infra.persistence.entity.UserEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.util.UUID

interface UserJpaRepository : JpaRepository<UserEntity, UUID> {
    @Query("SELECT c FROM UserEntity c WHERE c.id = :id")
    fun findByUserId(id: UUID): UserEntity?
}