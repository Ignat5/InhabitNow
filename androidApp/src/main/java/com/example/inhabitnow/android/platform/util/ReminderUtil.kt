package com.example.inhabitnow.android.platform.util

import java.security.MessageDigest
import kotlin.math.absoluteValue

object ReminderUtil {
    const val REMINDER_ID_KEY = "reminderId"

    fun stringToInteger(input: String): Int {
        val messageDigest = MessageDigest.getInstance("SHA-256")
        val hashBytes = messageDigest.digest(input.toByteArray())
        var result = 0
        for (i in hashBytes.indices) {
            result = result shl 8 or (hashBytes[i].toInt() and 0xFF)
        }
        return (result.absoluteValue) + 1
    }
}