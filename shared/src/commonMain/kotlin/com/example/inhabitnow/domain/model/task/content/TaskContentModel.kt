package com.example.inhabitnow.domain.model.task.content

import com.example.inhabitnow.core.type.ProgressLimitType
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime

sealed interface TaskContentModel {
    sealed interface ProgressContent : TaskContentModel {
        data class Number(
            val limitType: ProgressLimitType,
            val limitNumber: Double,
            val limitUnit: String
        ) : ProgressContent

        data class Time(
            val limitType: ProgressLimitType,
            val limitTime: LocalTime
        ) : ProgressContent
    }

    sealed interface FrequencyContent : TaskContentModel {
        data object EveryDay : FrequencyContent
        data class DaysOfWeek(val daysOfWeek: Set<DayOfWeek>) : FrequencyContent
    }

    sealed interface DateContent : TaskContentModel {
        data class Day(val date: LocalDate) : DateContent
        data class Period(
            val startDate: LocalDate,
            val endDate: LocalDate?
        ) : DateContent

    }
}