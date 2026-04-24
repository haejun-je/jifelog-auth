package com.jifelog.auth.application.port

import com.jifelog.auth.domain.User

interface SignupCommandPort {
    fun saveUser(user: User): User
    fun setUserId(userInfo: String)
}