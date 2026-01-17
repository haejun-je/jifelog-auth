package com.jifelog.auth.application

import com.jifelog.auth.application.command.ConfirmEmailVerificationCommand
import com.jifelog.auth.application.command.RegisterUserCommand
import com.jifelog.auth.application.command.RequestEmailVerificationCommand
import com.jifelog.auth.application.port.*
import com.jifelog.auth.common.HashUtils
import com.jifelog.auth.common.TokenUtils
import com.jifelog.auth.domain.PasswordAlgoType
import com.jifelog.auth.domain.User
import com.jifelog.auth.domain.UserPassword
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class SignupService(
    private val signupQueryPort : SignupQueryPort,
    private val signupCommandPort: SignupCommandPort,
    private val tokenCommandPort: TokenCommandPort,
    private val tokenQueryPort: TokenQueryPort,
    private val mailCommandPort: MailCommandPort,
    private val passwordHasher: PasswordHasher
) {
    private val log = LoggerFactory.getLogger(this::class.java)

    @Transactional
    fun registerUser(
        command: RegisterUserCommand
    ): User {
        // 이메일 인증 확인
        if (!tokenQueryPort.checkEmailVerified(command.email)) {

            // TODO: 예외처리 고도화 필요
            throw IllegalStateException("Email is not verified")
        }

        val userPassword = UserPassword.withoutId(
            passwordHasher.encode(command.password),
            PasswordAlgoType.ARGON2ID
        )

        val user = User.withoutId(
            command.username,
            command.email,
            userPassword
        )

        return signupCommandPort.saveUser(user)
    }

    fun requestEmailVerification(
        command: RequestEmailVerificationCommand
    ) {
        // 토큰 생성
        val token = TokenUtils.generateToken()
        val hashedToken = HashUtils.sha256Hex(token)

        // 토큰 저장
        tokenCommandPort.saveEmailVerificationToken(
            command.email,
            hashedToken,
            60 * 10
        )

        log.info("token: $token")

        // 인증 메일 발송
        /*mailCommandPort.sendVerificationMail(
            command.email,
            token
        )*/
    }

    fun confirmEmailVerification(
        command: ConfirmEmailVerificationCommand
    ) {
        val storedToken = tokenQueryPort.loadEmailVerificationToken(command.email)
        val token = HashUtils.sha256Hex(command.token)

        if (HashUtils.equalsHashed(storedToken, token)) {
            tokenCommandPort.saveEmailVerified(
                command.email,
                60 * 10
            )
        } else {
            // 유효하지 않은 토큰
            // TODO: 예외처리 고도화 필요
            throw RuntimeException("Invalid token: $storedToken")
        }

    }

    fun test(id: String): User{
        return signupQueryPort.loadUser(UUID.fromString(id))
    }
}