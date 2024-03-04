package com.example.inhabitnow.data.util

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

object DataConst {
    val distantFutureDate
        get() = Instant.DISTANT_FUTURE.toLocalDateTime(TimeZone.UTC).date
}