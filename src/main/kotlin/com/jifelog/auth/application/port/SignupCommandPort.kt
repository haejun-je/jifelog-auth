package com.jifelog.auth.application.port

import com.jifelog.auth.domain.Credential

interface SignupCommandPort {
    fun saveCredential(credential: Credential): Credential
    fun setUserId(userInfo: String)
}