package com.jifelog.auth.infra.persistence.adapter

import com.jifelog.auth.application.port.SignInQueryPort
import com.jifelog.auth.domain.User
import com.jifelog.auth.infra.persistence.mapper.UserMapper
import com.jifelog.auth.infra.persistence.repository.UserJpaRepository
import com.jifelog.auth.infra.persistence.repository.UserPasswordJpaRepository
import jakarta.persistence.EntityNotFoundException
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class SignInAdapter(
    private val userJpaRepository: UserJpaRepository,
    private val userPasswordJpaRepository: UserPasswordJpaRepository,
) : SignInQueryPort {
    override fun loadUser(id: UUID): User {
        val userEntity = userJpaRepository.findById(id)
            .orElseThrow { EntityNotFoundException("No user exists for id $id") }

        val passwordEntity = userPasswordJpaRepository.findById(id)
            .orElseThrow { EntityNotFoundException("No password exists for user id $id") }

        return UserMapper.toDomain(userEntity, passwordEntity)
    }

    override fun loadUser(email: String): User {
        val userEntity = userJpaRepository.findByEmail(email)
            ?: throw EntityNotFoundException("No user exists for email $email")

        val passwordEntity = userPasswordJpaRepository.findByUserId(userEntity.id!!)
            ?: throw EntityNotFoundException("No password exists for userId $userEntity.id")

        return UserMapper.toDomain(userEntity, passwordEntity)
    }
}