package com.jifelog.auth.application.port

import com.jifelog.auth.infra.session.UserSession

interface SessionQueryPort {
    fun getUser(): UserSession
}