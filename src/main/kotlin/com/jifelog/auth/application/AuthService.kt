package com.jifelog.auth.application

import com.jifelog.auth.application.port.AuthQueryPort
import com.jifelog.auth.domain.User
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class AuthService(
    private val authQueryPort : AuthQueryPort
) {
    fun test(): User{
        return authQueryPort.loadUser(UUID.fromString("019b5b63-481f-797d-b659-b708e9d48e37"))
    }
}