package com.jifelog.auth.infra.persistence.adapter

import com.jifelog.auth.application.port.AuthQueryPort
import com.jifelog.auth.domain.User
import com.jifelog.auth.infra.persistence.mapper.UserMapper
import com.jifelog.auth.infra.persistence.repository.UserJpaRepository
import jakarta.persistence.EntityNotFoundException
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class AuthAdapter(
    private val userJapRepository: UserJpaRepository
) : AuthQueryPort {
    override fun loadUser(id: UUID): User {
        return userJapRepository.findById(UUID.fromString("019b5b63-481f-797d-b659-b708e9d48e37"))
            .map(UserMapper::toDomain)
            .orElseThrow { EntityNotFoundException("No user exists for id $id") }
    }
}