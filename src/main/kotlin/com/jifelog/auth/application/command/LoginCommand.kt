package com.jifelog.auth.application.command

data class LoginCommand(
    val email: String,
    val password: String
)
