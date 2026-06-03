package com.jifelog.auth.infra.persistence.repository

import com.jifelog.auth.infra.persistence.entity.CredentialEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface CredentialRepository : JpaRepository<CredentialEntity, UUID> {
    fun findByLoginId(loginId: String): CredentialEntity?
}