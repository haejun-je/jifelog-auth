package com.jifelog.auth.application

import com.jifelog.auth.application.command.RegisterUserCommand
import com.jifelog.auth.application.port.AuthCommandPort
import com.jifelog.auth.application.port.AuthQueryPort
import com.jifelog.auth.domain.PasswordAlgoType
import com.jifelog.auth.domain.User
import com.jifelog.auth.domain.UserPassword
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@Transactional
class AuthService(
    private val authQueryPort : AuthQueryPort,
    private val authCommandPort: AuthCommandPort,
    private val passwordHasher: PasswordHasher
) {
    fun registerUser(
        registerUserCommand: RegisterUserCommand
    ): User {
        val userPassword = UserPassword.withoutId(
            passwordHasher.encode(registerUserCommand.password),
            PasswordAlgoType.ARGON2ID
        )

        val user = User.withoutId(
            registerUserCommand.username,
            registerUserCommand.email,
            userPassword
        )

        return authCommandPort.saveUser(user)
    }

    fun test(id: String): User{
        return authQueryPort.loadUser(UUID.fromString(id))
    }
}