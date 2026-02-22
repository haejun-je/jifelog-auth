package com.jifelog.auth.infra.persistence.adapter

import com.jifelog.auth.application.port.SignupCommandPort
import com.jifelog.auth.domain.User
import com.jifelog.auth.infra.persistence.mapper.UserMapper
import com.jifelog.auth.infra.persistence.mapper.UserPasswordMapper
import com.jifelog.auth.infra.persistence.repository.UserJpaRepository
import com.jifelog.auth.infra.persistence.repository.UserPasswordJpaRepository
import jakarta.servlet.http.HttpSession
import org.springframework.stereotype.Component

@Component
class SignupAdapter(
    private val userJapRepository: UserJpaRepository,
    private val userPasswordJpaRepository: UserPasswordJpaRepository,
    private val session: HttpSession // test
) : SignupCommandPort {
    override fun saveUser(user: User): User {
        val userEntity = userJapRepository.save(
            UserMapper.toEntity(user)
        )

        val passwordEntity = userPasswordJpaRepository.save(
            UserPasswordMapper.toEntity(
                userEntity.id!!,
                user.userPassword
            )
        )

        return UserMapper.toDomain(userEntity, passwordEntity)
    }

    override fun setUserId(userInfo: String) {
        session.setAttribute("userId", userInfo)
    }
}