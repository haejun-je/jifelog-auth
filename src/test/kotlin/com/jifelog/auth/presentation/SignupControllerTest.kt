package com.jifelog.auth.presentation

import com.jifelog.auth.common.exception.AuthException
import com.jifelog.auth.common.exception.ErrorCode
import com.jifelog.auth.domain.Credential
import com.jifelog.auth.domain.PasswordAlgoType
import com.jifelog.auth.presentation.request.SendEmailVerificationRequest
import com.jifelog.auth.presentation.request.SignupRequest
import com.jifelog.auth.support.AbstractControllerTest
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito
import org.mockito.kotlin.any
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.time.Instant
import java.util.*

class SignupControllerTest : AbstractControllerTest() {

    private fun createMockCredential(
        id: UUID = UUID.fromString("550e8400-e29b-41d4-a716-446655440000"),
        loginId: String = "testuser@example.com"
    ): Credential {
        return Credential.withId(
            id = id,
            userInfoId = UUID.fromString("660e8400-e29b-41d4-a716-446655440000"),
            loginId = loginId,
            passwordHash = "hashedPassword",
            passwordAlgo = PasswordAlgoType.ARGON2ID,
            passwordUpdatedAt = Instant.parse("2024-01-01T00:00:00Z"),
            failedCount = 0,
            lockedUntil = null,
            createdAt = Instant.parse("2024-01-01T00:00:00Z"),
            updatedAt = Instant.parse("2024-01-01T00:00:00Z")
        )
    }

    @Nested
    inner class `POST signup` {

        @Test
        fun `정상 응답 200`() {
            val payload = SignupRequest(
                "test@example.com",
                "nickname",
                "password123"
            )

            val uuid = UUID.randomUUID()

            val mockCredential = createMockCredential(
                id = uuid,
                loginId = payload.email,
            )

            BDDMockito.given(signupService.registerUser(any()))
                .willReturn(mockCredential)

            mockMvc.perform(
                post("/v1/signup")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jsonMapper.writeValueAsString(payload))
            )
                .andExpect(status().isOk)
                .andExpect(jsonPath("$.data.id").value(mockCredential.id.toString()))
                .andExpect(jsonPath("$.data.created_at").exists())
        }

        @Test
        fun `이미 가입된 이메일 409 C_01_002`() {
            BDDMockito.given(signupService.registerUser(any()))
                .willThrow(AuthException(ErrorCode.EC_01_002))

            val payload = SignupRequest("existing@example.com", "testuser", "password123")

            mockMvc.perform(
                post("/v1/signup")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jsonMapper.writeValueAsString(payload))
            )
                .andExpect(status().isConflict)
                .andExpect(jsonPath("$.data.error_code").value(ErrorCode.EC_01_002.name))
                .andExpect(jsonPath("$.data.message").value(ErrorCode.EC_01_002.defaultMessage))
        }

        @Test
        fun `이메일 인증 미완료 401 U_01_003`() {
            BDDMockito.given(signupService.registerUser(any()))
                .willThrow(AuthException(ErrorCode.EU_01_003))

            val payload = SignupRequest("test@example.com", "testuser", "password123")

            mockMvc.perform(
                post("/v1/signup")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jsonMapper.writeValueAsString(payload))
            )
                .andExpect(status().isUnauthorized)
                .andExpect(jsonPath("$.data.error_code").value(ErrorCode.EU_01_003.name))
                .andExpect(jsonPath("$.data.message").value(ErrorCode.EU_01_003.defaultMessage))
        }

        @Test
        fun `요청 필드 유효성 실패 400 B_00_001`() {
            mockMvc.perform(
                post("/v1/signup")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("""{"email": "not-a-valid-email", "nickname": "", "password": ""}""")
            )
                .andExpect(status().isBadRequest)
                .andExpect(jsonPath("$.data.error_code").value(ErrorCode.EB_00_001.name))
                .andExpect(jsonPath("$.data.details").isArray)
        }
    }

    @Nested
    inner class `POST signup email verify` {

        @Test
        fun `정상 응답 200`() {
            val payload = SendEmailVerificationRequest("test@example.com")

            mockMvc.perform(
                post("/v1/signup/email/verify")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jsonMapper.writeValueAsString(payload))
            )
                .andExpect(status().isOk)
                .andExpect(jsonPath("$.data").exists())
        }

        @Test
        fun `잘못된 이메일 형식 400 B_00_001`() {
            mockMvc.perform(
                post("/v1/signup/email/verify")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("""{"email": "invalid-email"}""")
            )
                .andExpect(status().isBadRequest)
                .andExpect(jsonPath("$.data.error_code").value(ErrorCode.EB_00_001.name))
        }
    }

    @Nested
    inner class `GET signup email verify` {

        @Test
        fun `정상 응답 200`() {
            mockMvc.perform(
                get("/v1/signup/email/verify")
                    .param("email", "test@example.com")
                    .param("token", "validtoken123456")
            )
                .andExpect(status().isOk)
                .andExpect(jsonPath("$.data").exists())
        }

        @Test
        fun `유효하지 않은 토큰 409 C_01_001`() {
            BDDMockito.willThrow(AuthException(ErrorCode.EC_01_001))
                .given(signupService).confirmEmailVerification(any())

            mockMvc.perform(
                get("/v1/signup/email/verify")
                    .param("email", "test@example.com")
                    .param("token", "invalid-token")
            )
                .andExpect(status().isConflict)
                .andExpect(jsonPath("$.data.error_code").value(ErrorCode.EC_01_001.name))
                .andExpect(jsonPath("$.data.message").value(ErrorCode.EC_01_001.defaultMessage))
        }
    }
}
