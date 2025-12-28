package com.jifelog.auth.infra.persistence.entity

import com.jifelog.auth.infra.persistence.entity.enums.PasswordAlgo
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.OffsetDateTime
import java.util.UUID

@Entity
@Table(schema = "auth", name = "user_password")
class UserPasswordEntity (
    @Id
    @Column(name = "user_id", columnDefinition = "uuid")
    val userId: UUID,

    @Column(name = "password_hash", nullable = false)
    val passwordHash: String,

    @Enumerated(EnumType.STRING)
    @Column(name = "password_algo", nullable = false)
    val passwordAlgo: PasswordAlgo = PasswordAlgo.ARGON2ID,

    @Column(name = "password_updated_at", nullable = false)
    val passwordUpdatedAt: OffsetDateTime,

    @Column(name = "failed_count", nullable = false)
    val failedCount: Int = 0,

    @Column(name = "locked_until")
    val lockedUntil: OffsetDateTime? = null,

    @Column(name = "updated_at", nullable = false)
    val updatedAt: OffsetDateTime
)