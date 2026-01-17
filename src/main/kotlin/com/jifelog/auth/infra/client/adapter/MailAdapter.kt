package com.jifelog.auth.infra.client.adapter

import com.jifelog.auth.application.port.MailCommandPort
import com.jifelog.auth.infra.client.ResendMailApiClient
import com.resend.services.emails.model.Template
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class MailAdapter(
    private val resendMailApiClient: ResendMailApiClient,
    @Value($$"${jifelog.mail.template-id.verification}")
    private val verificationTemplateId: String,
): MailCommandPort {
    override fun sendVerificationMail(to: String, token: String) {
        val mailTemplate = Template.builder()
            .id(verificationTemplateId)
            .variables(mapOf("AUTH_CODE" to token))
            .build()

        resendMailApiClient.sendMail(to, mailTemplate)
    }
}