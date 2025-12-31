package com.jifelog.auth.presentation.request

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank

data class SignupRequest(
    @Email
    val email: String,
    @NotBlank
    val username: String,
    @NotBlank
    val password: String
)
