package com.jifelog.auth.infra.persistence.adapter

import com.jifelog.auth.application.port.SignupCommandPort
import com.jifelog.auth.domain.Credential
import com.jifelog.auth.domain.User
import com.jifelog.auth.infra.persistence.mapper.CredentialMapper
import com.jifelog.auth.infra.persistence.mapper.UserMapper
import com.jifelog.auth.infra.persistence.repository.CredentialRepository
import com.jifelog.auth.infra.persistence.repository.UserJpaRepository
import org.springframework.stereotype.Component

@Component
class SignupAdapter(
    private val userJapRepository: UserJpaRepository,
    private val credentialRepository: CredentialRepository,
) : SignupCommandPort {
    override fun saveUser(user: User): User {
        val entity = userJapRepository.save(
            UserMapper.toEntity(user)
        )

        return UserMapper.toDomain(entity)
    }

    override fun saveCredential(credential: Credential): Credential {
        val credentialEntity = credentialRepository.save(
            CredentialMapper.toEntity(credential)
        )

        return CredentialMapper.toDomain(credentialEntity)
    }
}
