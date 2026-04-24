package com.jifelog.auth.presentation

import com.jifelog.auth.common.exception.AuthException
import com.jifelog.auth.common.exception.ErrorCode
import com.jifelog.auth.presentation.request.LoginRequest
import com.jifelog.auth.support.AbstractControllerTest
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito
import org.mockito.kotlin.any
import org.springframework.http.MediaType
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import com.jifelog.security.jwt.api.JifelogPrincipal

class AuthControllerTest : AbstractControllerTest() {

    @Nested
    inner class `POST login` {

        @Test
        fun `정상 응답 200`() {
            val payload = LoginRequest("test@example.com", "password123")

            mockMvc.perform(
                post("/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jsonMapper.writeValueAsString(payload))
            )
                .andExpect(status().isOk)
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data").exists())
        }

        @Test
        fun `잘못된 이메일 또는 비밀번호 401 U_01_001`() {
            BDDMockito.willThrow(AuthException(ErrorCode.EU_01_001))
                .given(loginService).login(any())

            val payload = LoginRequest("test@example.com", "wrongpassword")

            mockMvc.perform(
                post("/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jsonMapper.writeValueAsString(payload))
            )
                .andExpect(status().isUnauthorized)
                .andExpect(jsonPath("$.data.error_code").value(ErrorCode.EU_01_001.name))
                .andExpect(jsonPath("$.data.message").value(ErrorCode.EU_01_001.defaultMessage))
        }

        @Test
        fun `이메일 인증 미완료 401 U_01_003`() {
            BDDMockito.willThrow(AuthException(ErrorCode.EU_01_003))
                .given(loginService).login(any())

            val payload = LoginRequest("test@example.com", "password123")

            mockMvc.perform(
                post("/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jsonMapper.writeValueAsString(payload))
            )
                .andExpect(status().isUnauthorized)
                .andExpect(jsonPath("$.data.error_code").value(ErrorCode.EU_01_003.name))
                .andExpect(jsonPath("$.data.message").value(ErrorCode.EU_01_003.defaultMessage))
        }

        @Test
        fun `존재하지 않는 계정 404 N_01_001`() {
            BDDMockito.willThrow(AuthException(ErrorCode.EN_01_001))
                .given(loginService).login(any())

            val payload = LoginRequest("notfound@example.com", "password123")

            mockMvc.perform(
                post("/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jsonMapper.writeValueAsString(payload))
            )
                .andExpect(status().isNotFound)
                .andExpect(jsonPath("$.data.error_code").value(ErrorCode.EN_01_001.name))
                .andExpect(jsonPath("$.data.message").value(ErrorCode.EN_01_001.defaultMessage))
        }

        @Test
        fun `필수 필드 누락 400 B_00_001`() {
            mockMvc.perform(
                post("/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("""{"email": "", "password": ""}""")
            )
                .andExpect(status().isBadRequest)
                .andExpect(jsonPath("$.data.error_code").value(ErrorCode.EB_00_001.name))
                .andExpect(jsonPath("$.data.details").isArray)
        }

        @Test
        fun `잘못된 JSON 형식 400 B_00_002`() {
            mockMvc.perform(
                post("/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("not-valid-json{{{")
            )
                .andExpect(status().isBadRequest)
                .andExpect(jsonPath("$.data.error_code").value(ErrorCode.EB_00_002.name))
        }
    }

    @Nested
    inner class `GET user` {

        @Test
        fun `인증된 사용자 정보 반환 200`() {
            val principal = JifelogPrincipal(
                userId = "user-id-123",
                email = "test@example.com",
                username = "testuser",
                roles = setOf("ROLE_USER")
            )
            val auth = UsernamePasswordAuthenticationToken(
                principal, null, listOf(SimpleGrantedAuthority("ROLE_USER"))
            )

            mockMvc.perform(
                get("/user").with(authentication(auth))
            )
                .andExpect(status().isOk)
                .andExpect(jsonPath("$.data.user_id").value("user-id-123"))
                .andExpect(jsonPath("$.data.email").value("test@example.com"))
                .andExpect(jsonPath("$.data.username").value("testuser"))
        }

        @Test
        fun `인증 토큰 없음 401`() {
            mockMvc.perform(get("/user"))
                .andExpect(status().isUnauthorized)
        }
    }
}
