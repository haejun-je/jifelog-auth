package com.jifelog.auth.presentation

import com.jifelog.auth.application.LoginService
import com.jifelog.auth.common.exception.AuthException
import com.jifelog.auth.common.exception.ErrorCode
import com.jifelog.auth.config.JacksonConfig
import com.jifelog.auth.support.TestSecurityConfig
import com.jifelog.security.jwt.api.JifelogPrincipal
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito
import org.mockito.kotlin.any
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@WebMvcTest(AuthController::class)
@Import(TestSecurityConfig::class, JacksonConfig::class)
@TestPropertySource(properties = ["jifelog.security.jwt.secret=test-secret-key-for-testing-only-not-prod"])
class AuthControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockitoBean
    private lateinit var loginService: LoginService

    // ==================== POST /login ====================

    @Test
    fun `POST login - 정상 응답 200`() {
        mockMvc.perform(
            post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""{"email": "test@example.com", "password": "password123"}""")
        )
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.data").exists())
    }

    @Test
    fun `POST login - 잘못된 이메일 또는 비밀번호 401 U_01_001`() {
        BDDMockito.willThrow(AuthException(ErrorCode.U_01_001))
            .given(loginService).login(any())

        mockMvc.perform(
            post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""{"email": "test@example.com", "password": "wrongpassword"}""")
        )
            .andExpect(status().isUnauthorized)
            .andExpect(jsonPath("$.data.error_code").value("U_01_001"))
            .andExpect(jsonPath("$.data.message").value(ErrorCode.U_01_001.defaultMessage))
    }

    @Test
    fun `POST login - 이메일 인증 미완료 401 U_01_003`() {
        BDDMockito.willThrow(AuthException(ErrorCode.U_01_003))
            .given(loginService).login(any())

        mockMvc.perform(
            post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""{"email": "test@example.com", "password": "password123"}""")
        )
            .andExpect(status().isUnauthorized)
            .andExpect(jsonPath("$.data.error_code").value("U_01_003"))
            .andExpect(jsonPath("$.data.message").value(ErrorCode.U_01_003.defaultMessage))
    }

    @Test
    fun `POST login - 존재하지 않는 계정 404 N_01_001`() {
        BDDMockito.willThrow(AuthException(ErrorCode.N_01_001))
            .given(loginService).login(any())

        mockMvc.perform(
            post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""{"email": "notfound@example.com", "password": "password123"}""")
        )
            .andExpect(status().isNotFound)
            .andExpect(jsonPath("$.data.error_code").value("N_01_001"))
            .andExpect(jsonPath("$.data.message").value(ErrorCode.N_01_001.defaultMessage))
    }

    @Test
    fun `POST login - 필수 필드 누락 400 B_00_001`() {
        mockMvc.perform(
            post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""{"email": "", "password": ""}""")
        )
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.data.error_code").value("B_00_001"))
            .andExpect(jsonPath("$.data.details").isArray)
    }

    @Test
    fun `POST login - 잘못된 JSON 형식 400 B_00_002`() {
        mockMvc.perform(
            post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("not-valid-json{{{")
        )
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.data.error_code").value("B_00_002"))
    }

    // ==================== GET /user ====================

    @Test
    fun `GET user - 인증된 사용자 정보 반환 200`() {
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
    fun `GET user - 인증 토큰 없음 401`() {
        mockMvc.perform(get("/user"))
            .andExpect(status().isUnauthorized)
    }
}
