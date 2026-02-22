package com.jifelog.auth.presentation

import com.jifelog.auth.application.LoginService
import com.jifelog.auth.application.command.LoginCommand
import com.jifelog.auth.presentation.request.LoginRequest
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
    ) {
        loginService.login(
            LoginCommand(
                request.email,
                request.password
            )
        )
    }


    @GetMapping("/user")
    fun test(
        @JifelogUser user: JifelogUserData
    ): ResponseEntity<JifelogUserData> {
        return ResponseEntity.ok(user)
    }
}
