package com.jifelog.auth.presentation

import com.jifelog.auth.application.SignupService
import com.jifelog.auth.application.command.ConfirmEmailVerificationCommand
import com.jifelog.auth.application.command.RegisterUserCommand
import com.jifelog.auth.application.command.RequestEmailVerificationCommand
import com.jifelog.auth.presentation.request.SendEmailVerificationRequest
import com.jifelog.auth.presentation.request.SignupRequest
import com.jifelog.auth.presentation.response.SignupResponse
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class SignupController(
    private val signupService: SignupService
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

        val result = signupService.registerUser(command)

        return ResponseEntity.ok(
            SignupResponse(
                result.id!!,
                result.username,
                result.createdAt
            )
        )
    }

    @PostMapping("/signup/email/verify")
    fun sendVerificationEmail(
        @RequestBody @Valid request: SendEmailVerificationRequest
    ) {
        signupService.requestEmailVerification(
            RequestEmailVerificationCommand(
                request.email
            )
        )
    }

    @GetMapping("/signup/email/verify")
    fun verifyEmail(
        @RequestParam email: String,
        @RequestParam token: String
    ) {
        signupService.confirmEmailVerification(
            ConfirmEmailVerificationCommand(
                email,
                token
            )
        )
    }

    @GetMapping("/test/{id}")
    fun test(
        @PathVariable id: String
    ): ResponseEntity<SignupResponse> {
        val result = signupService.test(id)

        return ResponseEntity.ok(SignupResponse(result.id!!, result.username, result.createdAt))
    }
}