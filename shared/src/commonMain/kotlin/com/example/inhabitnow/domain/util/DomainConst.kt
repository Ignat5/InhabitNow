package com.example.inhabitnow.domain.util

import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

object DomainConst {
    val distantFutureEpochDay
        get() = Instant.DISTANT_FUTURE.toLocalDateTime(TimeZone.UTC).date.toEpochDays().toLong()

}