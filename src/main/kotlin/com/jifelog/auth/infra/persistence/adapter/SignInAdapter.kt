package com.jifelog.auth.infra.persistence.adapter

import com.jifelog.auth.application.port.SignInQueryPort
import com.jifelog.auth.common.exception.AuthException
import com.jifelog.auth.common.exception.ErrorCode
import com.jifelog.auth.domain.User
import com.jifelog.auth.infra.persistence.mapper.UserMapper
import com.jifelog.auth.infra.persistence.repository.UserJpaRepository
import com.jifelog.auth.infra.persistence.repository.UserPasswordJpaRepository
import org.springframework.stereotype.Component

@Component
class SignInAdapter(
    private val userJpaRepository: UserJpaRepository,
    private val userPasswordJpaRepository: UserPasswordJpaRepository,
) : SignInQueryPort {
    override fun loadUser(email: String): User {
        val userEntity = userJpaRepository.findByEmail(email)
            ?: throw AuthException(ErrorCode.N_01_001)

        val passwordEntity = userPasswordJpaRepository.findByUserId(userEntity.id!!)
            ?: throw AuthException(ErrorCode.N_01_001)

        return UserMapper.toDomain(userEntity, passwordEntity)
    }
}