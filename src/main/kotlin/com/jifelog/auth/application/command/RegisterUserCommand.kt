package com.jifelog.auth.application.command

data class RegisterUserCommand(
    val email: String,
    val username: String,
    val password: String
)
