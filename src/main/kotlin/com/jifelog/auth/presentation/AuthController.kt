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
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/{version}")
class AuthController(
    private val loginService: LoginService
) {
    @GetMapping("/user")
    fun getUser(
        @JifelogUser user: JifelogUserData
    ): ResponseEntity<ApiResponse<JifelogUserData>> =
        ResponseEntity.ok(ApiResponse.of(user))

    @PostMapping(version = "1", path = ["/login"])
    fun login(
        @RequestBody @Valid request: LoginRequest
    ): ResponseEntity<ApiResponse<Empty>> {
        loginService.login(
            LoginCommand(
                request.loginId,
                request.password
            )
        )
        return ResponseEntity.ok(ApiResponse.empty())
    }
}
