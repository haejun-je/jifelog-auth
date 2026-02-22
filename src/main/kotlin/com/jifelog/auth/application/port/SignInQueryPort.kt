package com.jifelog.auth.application.port

import com.jifelog.auth.domain.User
import java.util.UUID

interface SignInQueryPort {
    fun loadUser(id: UUID): User
    fun loadUser(email: String): User
}