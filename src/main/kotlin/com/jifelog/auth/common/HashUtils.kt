package com.jifelog.auth.common

import java.security.MessageDigest
import java.util.Base64

object HashUtils {
    fun sha256Hex(input: String): String =
        MessageDigest.getInstance("SHA-256")
            .digest(input.toByteArray(Charsets.UTF_8))
            .joinToString("") { "%02x".format(it) }

    fun sha256Base64Url(input: String): String =
        MessageDigest.getInstance("SHA-256")
            .digest(input.toByteArray(Charsets.UTF_8))
            .let {
                Base64.getUrlEncoder().withoutPadding().encodeToString(it)
            }


    fun equalsHashed(aHashed: String, bHashed: String): Boolean {
        return MessageDigest.isEqual(
            aHashed.toByteArray(Charsets.UTF_8),
            bHashed.toByteArray(Charsets.UTF_8)
        )
    }
}