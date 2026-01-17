package com.jifelog.auth.application.port

interface MailCommandPort {
    fun sendVerificationMail(to: String, token: String)
}