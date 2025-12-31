package com.jifelog.auth.presentation.response

import java.time.Instant
import java.util.UUID

data class SignupResponse(
    val id: UUID,
    val username: String,
    val createdAt: Instant
)
