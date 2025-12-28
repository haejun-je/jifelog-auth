package com.jifelog.auth.presentation

import com.jifelog.auth.application.AuthService
import com.jifelog.auth.presentation.response.TestResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth")
class AuthController(
    private val authService: AuthService
) {
    @GetMapping("/test")
    fun test(): ResponseEntity<TestResponse> {
        val test = authService.test()

        return ResponseEntity.ok(
            TestResponse(
                test.id,
                test.username
            )
        )
    }
}