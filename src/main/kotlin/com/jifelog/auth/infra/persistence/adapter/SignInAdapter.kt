package com.jifelog.auth.infra.persistence.adapter

import com.jifelog.auth.application.port.SignInQueryPort
import com.jifelog.auth.common.exception.AuthException
import com.jifelog.auth.common.exception.ErrorCode
import com.jifelog.auth.domain.Credential
import com.jifelog.auth.domain.User
import com.jifelog.auth.infra.persistence.mapper.CredentialMapper
import com.jifelog.auth.infra.persistence.mapper.UserMapper
import com.jifelog.auth.infra.persistence.repository.CredentialRepository
import com.jifelog.auth.infra.persistence.repository.UserJpaRepository
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class SignInAdapter(
    private val userJpaRepository: UserJpaRepository,
    private val credentialRepository: CredentialRepository,
) : SignInQueryPort {
    override fun loadCredential(loginId: String): Credential {
        val credentialEntity = credentialRepository.findByLoginId(loginId)
            ?: throw AuthException(ErrorCode.EN_01_001)

        return CredentialMapper.toDomain(credentialEntity)
    }

    override fun loadUser(userId: UUID): User {
        val userEntity = userJpaRepository.findByUserId(userId)
            ?: throw AuthException(ErrorCode.EN_01_001)

        return UserMapper.toDomain(userEntity)
    }
}
