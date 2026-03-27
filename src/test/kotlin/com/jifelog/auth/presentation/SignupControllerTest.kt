package com.jifelog.auth.presentation

import com.jifelog.auth.application.SignupService
import com.jifelog.auth.common.exception.AuthException
import com.jifelog.auth.common.exception.ErrorCode
import com.jifelog.auth.config.JacksonConfig
import com.jifelog.auth.domain.PasswordAlgoType
import com.jifelog.auth.domain.User
import com.jifelog.auth.domain.UserPassword
import com.jifelog.auth.domain.UserStatusType
import com.jifelog.auth.support.TestSecurityConfig
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito
import org.mockito.kotlin.any
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.time.Instant
import java.util.*

@WebMvcTest(SignupController::class)
@Import(TestSecurityConfig::class, JacksonConfig::class)
@TestPropertySource(properties = ["jifelog.security.jwt.secret=test-secret-key-for-testing-only-not-prod"])
class SignupControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockitoBean
    private lateinit var signupService: SignupService

    private fun createMockUser(
        id: UUID = UUID.fromString("550e8400-e29b-41d4-a716-446655440000"),
        username: String = "testuser",
        email: String = "test@example.com"
    ): User {
        val userPassword = UserPassword.withoutId("hashedPassword", PasswordAlgoType.ARGON2ID)
        return User.withId(
            id = id,
            username = username,
            email = email,
            status = UserStatusType.ACTIVE,
            createdAt = Instant.parse("2024-01-01T00:00:00Z"),
            updatedAt = Instant.parse("2024-01-01T00:00:00Z"),
            deletedAt = null,
            userPassword = userPassword
        )
    }

    // ==================== POST /signup ====================

    @Test
    fun `POST signup - 정상 응답 200`() {
        BDDMockito.given(signupService.registerUser(any()))
            .willReturn(createMockUser())

        mockMvc.perform(
            post("/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""{"email": "test@example.com", "username": "testuser", "password": "password123"}""")
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.data.id").value("550e8400-e29b-41d4-a716-446655440000"))
            .andExpect(jsonPath("$.data.username").value("testuser"))
            .andExpect(jsonPath("$.data.created_at").exists())
    }

    @Test
    fun `POST signup - 이미 가입된 이메일 409 C_01_002`() {
        BDDMockito.given(signupService.registerUser(any()))
            .willThrow(AuthException(ErrorCode.C_01_002))

        mockMvc.perform(
            post("/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""{"email": "existing@example.com", "username": "testuser", "password": "password123"}""")
        )
            .andExpect(status().isConflict)
            .andExpect(jsonPath("$.data.error_code").value("C_01_002"))
            .andExpect(jsonPath("$.data.message").value(ErrorCode.C_01_002.defaultMessage))
    }

    @Test
    fun `POST signup - 이메일 인증 미완료 401 U_01_003`() {
        BDDMockito.given(signupService.registerUser(any()))
            .willThrow(AuthException(ErrorCode.U_01_003))

        mockMvc.perform(
            post("/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""{"email": "test@example.com", "username": "testuser", "password": "password123"}""")
        )
            .andExpect(status().isUnauthorized)
            .andExpect(jsonPath("$.data.error_code").value("U_01_003"))
            .andExpect(jsonPath("$.data.message").value(ErrorCode.U_01_003.defaultMessage))
    }

    @Test
    fun `POST signup - 요청 필드 유효성 실패 400 B_00_001`() {
        mockMvc.perform(
            post("/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""{"email": "not-a-valid-email", "username": "", "password": ""}""")
        )
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.data.error_code").value("B_00_001"))
            .andExpect(jsonPath("$.data.details").isArray)
    }

    // ==================== POST /signup/email/verify ====================

    @Test
    fun `POST signup email verify - 정상 응답 200`() {
        mockMvc.perform(
            post("/signup/email/verify")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""{"email": "test@example.com"}""")
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.data").exists())
    }

    @Test
    fun `POST signup email verify - 잘못된 이메일 형식 400 B_00_001`() {
        mockMvc.perform(
            post("/signup/email/verify")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""{"email": "invalid-email"}""")
        )
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.data.error_code").value("B_00_001"))
    }

    // ==================== GET /signup/email/verify ====================

    @Test
    fun `GET signup email verify - 정상 응답 200`() {
        mockMvc.perform(
            get("/signup/email/verify")
                .param("email", "test@example.com")
                .param("token", "validtoken123456")
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.data").exists())
    }

    @Test
    fun `GET signup email verify - 유효하지 않은 토큰 409 C_01_001`() {
        BDDMockito.willThrow(AuthException(ErrorCode.C_01_001))
            .given(signupService).confirmEmailVerification(any())

        mockMvc.perform(
            get("/signup/email/verify")
                .param("email", "test@example.com")
                .param("token", "invalid-token")
        )
            .andExpect(status().isConflict)
            .andExpect(jsonPath("$.data.error_code").value("C_01_001"))
            .andExpect(jsonPath("$.data.message").value(ErrorCode.C_01_001.defaultMessage))
    }
}
