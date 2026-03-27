package com.jifelog.auth.application

import com.jifelog.auth.application.command.LoginCommand
import com.jifelog.auth.application.port.SessionCommandPort
import com.jifelog.auth.application.port.SessionQueryPort
import com.jifelog.auth.application.port.SignInQueryPort
import com.jifelog.auth.common.exception.AuthException
import com.jifelog.auth.common.exception.ErrorCode
import com.jifelog.auth.infra.session.UserSession
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
        val user = signInQueryPort.loadUser(command.email)

        val isValidPassword = passwordHasher.matches(
            command.password,
            user.userPassword.passwordHash
        )

        if (isValidPassword) {
            sessionCommandPort.registerUserSession(user)
        } else {
            throw AuthException(ErrorCode.U_01_001)
        }
    }
}