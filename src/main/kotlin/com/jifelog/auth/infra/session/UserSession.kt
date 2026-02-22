package com.jifelog.auth.infra.session

import java.util.UUID

data class UserSession(
    val id: UUID,
    val username: String,
    val email: String,
    val testName: String? = "test"
)
