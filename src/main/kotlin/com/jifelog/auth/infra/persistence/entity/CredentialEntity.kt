package com.jifelog.auth.infra.persistence.entity

import com.jifelog.auth.infra.persistence.entity.enums.PasswordAlgo
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.hibernate.annotations.DynamicInsert
import java.time.Instant
import java.util.UUID

@Entity
@DynamicInsert
@Table(schema = "auth", name = "credential")
class CredentialEntity(
    @Id
    @Column(name = "id", columnDefinition = "uuid", updatable = false)
    val id: UUID,

    @Column(name = "user_info_id", columnDefinition = "uuid", nullable = false, updatable = false)
    val userInfoId: UUID,

    @Column(name = "login_id", length = 100, nullable = false, unique = true)
    val loginId: String,

    @Column(name = "password_hash", columnDefinition = "text", nullable = false)
    val passwordHash: String,

    @Enumerated(EnumType.STRING)
    @Column(name = "password_algo", length = 30, nullable = false)
    val passwordAlgo: PasswordAlgo = PasswordAlgo.ARGON2ID,

    @Column(name = "password_updated_at", nullable = false)
    val passwordUpdatedAt: Instant,

    @Column(name = "failed_count", nullable = false)
    val failedCount: Int = 0,

    @Column(name = "locked_until")
    val lockedUntil: Instant? = null,

    @Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: Instant,

    @Column(name = "updated_at", nullable = false)
    val updatedAt: Instant
)