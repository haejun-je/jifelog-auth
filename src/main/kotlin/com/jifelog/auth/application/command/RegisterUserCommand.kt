package com.jifelog.auth.application.command

data class RegisterUserCommand(
    val email: String,
    val nickname: String,
    val password: String
)
