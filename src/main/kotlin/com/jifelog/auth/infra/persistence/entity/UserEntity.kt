package com.jifelog.auth.infra.persistence.entity

import com.jifelog.auth.infra.persistence.entity.enums.UserStatus
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.OffsetDateTime
import java.util.UUID

@Entity
@Table(schema = "auth", name = "user")
class UserEntity(

    @Id
    @Column(name = "id", columnDefinition = "uuid", updatable = false)
    val id: UUID,

    @Column(name = "username", length = 50, nullable = false)
    val username: String,

    @Column(name = "email", length = 254)
    val email: String? = null,

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    val status: UserStatus = UserStatus.ACTIVE,

    @Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: OffsetDateTime,

    @Column(name = "updated_at", nullable = false)
    val updatedAt: OffsetDateTime,

    @Column(name = "deleted_at")
    val deletedAt: OffsetDateTime? = null
)