package com.jifelog.auth.presentation

import com.jifelog.auth.application.AuthService
import com.jifelog.auth.application.command.RegisterUserCommand
import com.jifelog.auth.presentation.request.SignupRequest
import com.jifelog.auth.presentation.response.SignupResponse
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class AuthController(
    private val authService: AuthService
) {
    @PostMapping("/signup")
    fun signup(
        @RequestBody @Valid request: SignupRequest
    ): ResponseEntity<SignupResponse> {
        val command = RegisterUserCommand(
            request.email,
            request.username,
            request.password
        )

        val result = authService.registerUser(command)

        return ResponseEntity.ok(
            SignupResponse(
                result.id!!,
                result.username,
                result.createdAt
            )
        )
    }

    @GetMapping("/test/{id}")
    fun test(
        @PathVariable id: String
    ): ResponseEntity<SignupResponse> {
        val result = authService.test(id)

        return ResponseEntity.ok(SignupResponse(result.id!!, result.username, result.createdAt))
    }
}