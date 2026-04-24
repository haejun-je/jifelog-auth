package com.jifelog.auth.presentation

import com.jifelog.auth.application.SignupService
import com.jifelog.auth.application.command.ConfirmEmailVerificationCommand
import com.jifelog.auth.application.command.RegisterUserCommand
import com.jifelog.auth.application.command.RequestEmailVerificationCommand
import com.jifelog.auth.presentation.request.SendEmailVerificationRequest
import com.jifelog.auth.presentation.request.SignupRequest
import com.jifelog.auth.presentation.response.ApiResponse
import com.jifelog.auth.presentation.response.Empty
import com.jifelog.auth.presentation.response.SignupResponse
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
class SignupController(
    private val signupService: SignupService
) {
    @PostMapping("/signup")
    fun signup(
        @RequestBody @Valid request: SignupRequest
    ): ResponseEntity<ApiResponse<SignupResponse>> {
        val command = RegisterUserCommand(
            request.email,
            request.username,
            request.password
        )

        val result = signupService.registerUser(command)

        return ResponseEntity.ok(
            ApiResponse.of(
                SignupResponse(
                    result.id!!,
                    result.username,
                    result.createdAt
                )
            )
        )
    }

    @PostMapping("/signup/email/verify")
    fun sendVerificationEmail(
        @RequestBody @Valid request: SendEmailVerificationRequest
    ): ResponseEntity<ApiResponse<Empty>> {
        signupService.requestEmailVerification(
            RequestEmailVerificationCommand(
                request.email
            )
        )

        return ResponseEntity.ok(ApiResponse.empty())
    }

    @GetMapping("/signup/email/verify")
    fun verifyEmail(
        @RequestParam email: String,
        @RequestParam token: String
    ): ResponseEntity<ApiResponse<Empty>> {
        signupService.confirmEmailVerification(
            ConfirmEmailVerificationCommand(
                email,
                token
            )
        )
        return ResponseEntity.ok(ApiResponse.empty())
    }
}
