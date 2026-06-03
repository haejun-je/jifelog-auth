package com.jifelog.auth.infra.persistence.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.Instant
import java.util.UUID

@Entity
@Table(schema = "account", name = "user_info")
data class UserEntity(
    @Id
    var id: UUID,

    @Column(nullable = false, length = 50)
    var nickname: String,

    @Column(nullable = true, length = 50)
    var username: String,

    @Column(name = "profile_img", nullable = false, length = 50)
    var profileImg: String,

    @Column(name = "created_at", nullable = false, updatable = false)
    var createdAt: Instant,

    @Column(name = "updated_at", nullable = false)
    var updatedAt: Instant,
)