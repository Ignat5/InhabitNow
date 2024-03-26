package com.example.inhabitnow.domain.util

import com.example.inhabitnow.core.type.ProgressLimitType
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

object DomainConst {
    const val DEFAULT_TASK_TITLE = ""
    const val DEFAULT_TASK_DESCRIPTION = ""
    const val DEFAULT_PRIORITY = 1
    val DEFAULT_LIMIT_TYPE
        get() = ProgressLimitType.AtLeast
    const val DEFAULT_LIMIT_NUMBER: Double = 0.0
    val DEFAULT_LIMIT_TIME
        get() = LocalTime(0, 0, 0)
    const val DEFAULT_LIMIT_UNIT = ""
    const val DEFAULT_IS_ARCHIVED = false
}