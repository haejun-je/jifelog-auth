package com.jifelog.auth.application.command

data class LoginCommand(
    val loginId: String,
    val password: String
)
