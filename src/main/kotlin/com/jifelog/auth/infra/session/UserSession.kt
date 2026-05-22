package com.jifelog.auth.infra.session

import java.util.UUID

data class UserSession(
    val userId: UUID,
    val nickname: String,
    val username: String
)
