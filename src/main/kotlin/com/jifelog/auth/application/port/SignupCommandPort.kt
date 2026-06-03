package com.jifelog.auth.application.port

import com.jifelog.auth.domain.Credential
import com.jifelog.auth.domain.User

interface SignupCommandPort {
    fun saveUser(user: User): User
    fun saveCredential(credential: Credential): Credential
}