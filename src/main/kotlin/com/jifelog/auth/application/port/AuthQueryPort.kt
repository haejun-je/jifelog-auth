package com.jifelog.auth.application.port

import com.jifelog.auth.domain.User
import java.util.UUID

interface AuthQueryPort {
    fun loadUser(id: UUID): User
}