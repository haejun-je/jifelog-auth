package com.jifelog.auth.common.exception

import org.springframework.http.HttpStatus

enum class ErrorCode(val httpStatus: HttpStatus, val defaultMessage: String) {
    // 400 Bad Request
    EB_00_001(HttpStatus.BAD_REQUEST, "요청 값이 올바르지 않습니다."),
    EB_00_002(HttpStatus.BAD_REQUEST, "요청을 읽을 수 없습니다."),

    // 401 Unauthorized
    EU_01_001(HttpStatus.UNAUTHORIZED, "이메일 또는 비밀번호가 올바르지 않습니다."),
    EU_01_002(HttpStatus.UNAUTHORIZED, "인증 토큰이 만료되었거나 존재하지 않습니다."),
    EU_01_003(HttpStatus.UNAUTHORIZED, "이메일 인증이 완료되지 않았습니다."),

    // 404 Not Found
    EN_01_001(HttpStatus.NOT_FOUND, "존재하지 않는 계정입니다."),

    // 409 Conflict
    EC_01_001(HttpStatus.CONFLICT, "유효하지 않은 토큰입니다."),
    EC_01_002(HttpStatus.CONFLICT, "이미 가입된 이메일입니다."),

    // 500 Server Error
    ES_00_001(HttpStatus.INTERNAL_SERVER_ERROR, "서버 오류가 발생했습니다."),
}
