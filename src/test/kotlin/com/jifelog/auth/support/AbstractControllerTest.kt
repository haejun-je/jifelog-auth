package com.jifelog.auth.support

import com.jifelog.auth.application.LoginService
import com.jifelog.auth.application.SignupService
import com.jifelog.auth.config.JacksonConfig
import com.jifelog.auth.presentation.AuthController
import com.jifelog.auth.presentation.SignupController
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.MockMvc
import tools.jackson.databind.json.JsonMapper

@WebMvcTest(AuthController::class, SignupController::class)
@Import(TestSecurityConfig::class, JacksonConfig::class)
@TestPropertySource(properties = ["jifelog.security.jwt.secret=test-secret-key-for-testing-only-not-prod"])
abstract class AbstractControllerTest {

    @Autowired
    protected lateinit var mockMvc: MockMvc

    @Autowired
    protected lateinit var jsonMapper: JsonMapper

    @MockitoBean
    protected lateinit var loginService: LoginService

    @MockitoBean
    protected lateinit var signupService: SignupService
}
