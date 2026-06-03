package com.jifelog.auth.application.port

import com.jifelog.auth.domain.Credential
import com.jifelog.auth.domain.User
import java.util.UUID

interface SignInQueryPort {
    fun loadCredential(loginId: String): Credential
    fun loadUser(userId: UUID): User
}