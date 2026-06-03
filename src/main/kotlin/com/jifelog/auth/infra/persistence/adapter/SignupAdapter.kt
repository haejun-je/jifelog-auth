package com.jifelog.auth.infra.persistence.adapter

import com.jifelog.auth.application.port.SignupCommandPort
import com.jifelog.auth.domain.Credential
import com.jifelog.auth.domain.User
import com.jifelog.auth.infra.persistence.mapper.CredentialMapper
import com.jifelog.auth.infra.persistence.mapper.UserMapper
import com.jifelog.auth.infra.persistence.repository.CredentialRepository
import com.jifelog.auth.infra.persistence.repository.UserJpaRepository
import jakarta.servlet.http.HttpSession
import org.springframework.stereotype.Component

@Component
class SignupAdapter(
    private val userJapRepository: UserJpaRepository,
    private val credentialRepository: CredentialRepository,
    private val session: HttpSession // test
) : SignupCommandPort {
    override fun saveCredential(credential: Credential): Credential {
        val credentialEntity = credentialRepository.save(
            CredentialMapper.toEntity(credential)
        )

        return CredentialMapper.toDomain(credentialEntity)
    }

    override fun setUserId(userInfo: String) {
        session.setAttribute("userId", userInfo)
    }
}
