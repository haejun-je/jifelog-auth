package com.jifelog.auth.infra.client

import com.resend.Resend
import com.resend.core.exception.ResendException
import com.resend.services.emails.model.CreateEmailOptions
import com.resend.services.emails.model.Template
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class ResendMailApiClient(
    @Value($$"${jifelog.mail.api-key}")
    private val apiKey: String
) {
    private val log = LoggerFactory.getLogger(this::class.java)

     fun sendMail(to: String, template: Template) {
        val resend = Resend(apiKey)

        val params = CreateEmailOptions.builder()
            .to(to)
            .template(template)
            .build()

        try {
            resend.emails().send(params)
        } catch (e: ResendException) {
            log.error("Error sending mail", e)
        }
    }
}