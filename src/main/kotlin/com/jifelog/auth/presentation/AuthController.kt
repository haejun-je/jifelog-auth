package com.jifelog.auth.presentation

import com.jifelog.auth.application.LoginService
import com.jifelog.auth.application.command.LoginCommand
import com.jifelog.auth.presentation.request.LoginRequest
import com.jifelog.auth.presentation.response.ApiResponse
import com.jifelog.auth.presentation.response.Empty
import com.jifelog.security.jwt.api.JifelogUser
import com.jifelog.security.jwt.api.JifelogUserData
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class AuthController(
    private val loginService: LoginService
) {
    @PostMapping("/login")
    fun login(
        @RequestBody @Valid request: LoginRequest
    ): ResponseEntity<ApiResponse<Empty>> {
        loginService.login(
            LoginCommand(
                request.email,
                request.password
            )
        )
        return ResponseEntity.ok(ApiResponse.empty())
    }

    @GetMapping("/user")
    fun getUser(
        @JifelogUser user: JifelogUserData
    ): ResponseEntity<ApiResponse<JifelogUserData>> =
        ResponseEntity.ok(ApiResponse.of(user))
}
