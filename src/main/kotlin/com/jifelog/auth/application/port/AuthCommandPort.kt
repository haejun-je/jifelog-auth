package com.jifelog.auth.application.port

import com.jifelog.auth.domain.User

interface AuthCommandPort {
    fun saveUser(user: User): User
}