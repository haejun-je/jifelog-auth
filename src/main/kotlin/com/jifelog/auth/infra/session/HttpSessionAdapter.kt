package com.jifelog.auth.infra.session

import com.jifelog.auth.application.port.SessionCommandPort
import com.jifelog.auth.application.port.SessionQueryPort
import com.jifelog.auth.domain.User
import jakarta.servlet.http.HttpSession
import org.springframework.session.FindByIndexNameSessionRepository
import org.springframework.stereotype.Component
import tools.jackson.databind.json.JsonMapper

@Component
class HttpSessionAdapter(
    private val session: HttpSession,
    private val jsonMapper: JsonMapper
) : SessionCommandPort, SessionQueryPort {
    companion object {
        const val USER_SESSION = "USER_SESSION"
    }

    override fun registerUserSession(user: User) {
        // Indexed 인덱스 키
        session.setAttribute(
            FindByIndexNameSessionRepository.PRINCIPAL_NAME_INDEX_NAME,
            user.id.toString()
        )

        session.setAttribute(
            USER_SESSION,
            UserSession(
                user.id!!,
                user.username,
                user.email,
            )
        )
    }

    override fun getUser(): UserSession {
        //val sessionMap = session.getAttribute(USER_SESSION) as LinkedHashMap<*, *>

        /*return UserSession(
            UUID.fromString(sessionMap["id"] as String),
            sessionMap["username"] as String,
            sessionMap["email"] as String
        )*/

        val raw = session.getAttribute(USER_SESSION)
        return jsonMapper.convertValue(raw, UserSession::class.java)
    }
}
