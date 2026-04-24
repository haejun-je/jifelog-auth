package com.jifelog.auth.application.port

import com.jifelog.auth.domain.User

interface SignInQueryPort {
    fun loadUser(email: String): User
}