package com.jifelog.auth.application

import com.jifelog.auth.application.command.LoginCommand
import com.jifelog.auth.application.port.SessionCommandPort
import com.jifelog.auth.application.port.SessionQueryPort
import com.jifelog.auth.application.port.SignInQueryPort
import com.jifelog.auth.common.exception.AuthException
import com.jifelog.auth.common.exception.ErrorCode
import org.springframework.stereotype.Service

@Service
class LoginService(
    private val sessionCommandPort: SessionCommandPort,
    private val sessionQueryPort: SessionQueryPort,
    private val passwordHasher: PasswordHasher,
    private val signInQueryPort: SignInQueryPort
) {
    fun login(
        command: LoginCommand
    ) {
        val credential = signInQueryPort.loadCredential(command.loginId)

        val isValidPassword = passwordHasher.matches(
            command.password,
            credential.passwordHash
        )

        if (isValidPassword) {
            val user = signInQueryPort.loadUser(credential.userInfoId)

            sessionCommandPort.registerUserSession(user)
        } else {
            throw AuthException(ErrorCode.EU_01_001)
        }
    }
}