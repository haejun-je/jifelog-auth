package com.jifelog.auth.infra.persistence.entity

import com.jifelog.auth.infra.persistence.entity.enums.UserStatus
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.hibernate.annotations.DynamicInsert
import org.hibernate.annotations.UuidGenerator
import java.time.Instant
import java.util.UUID

@Entity
@DynamicInsert
@Table(schema = "auth", name = "user")
class UserEntity(
    @Id
    @UuidGenerator(style = UuidGenerator.Style.VERSION_7)
    @Column(name = "id", columnDefinition = "uuid", updatable = false)
    val id: UUID? = null,

    @Column(name = "username", nullable = false)
    val username: String,

    @Column(name = "email")
    val email: String,

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    val status: UserStatus = UserStatus.ACTIVE,

    @Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: Instant,

    @Column(name = "updated_at", nullable = false)
    val updatedAt: Instant,

    @Column(name = "deleted_at")
    val deletedAt: Instant? = null
)