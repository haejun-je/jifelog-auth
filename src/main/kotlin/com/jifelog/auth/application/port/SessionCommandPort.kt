package com.jifelog.auth.application.port

import com.jifelog.auth.domain.User

interface SessionCommandPort {
    fun registerUserSession(user: User)
}