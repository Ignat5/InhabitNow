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

    sealed class FrequencyContent(val type: Type) : TaskContentModel {
        enum class Type { EveryDay, DaysOfWeek }

        data object EveryDay : FrequencyContent(Type.EveryDay)
        data class DaysOfWeek(val daysOfWeek: Set<DayOfWeek>) : FrequencyContent(Type.DaysOfWeek)
    }

    sealed interface DateContent : TaskContentModel {
        data class Day(val date: LocalDate) : DateContent
        data class Period(
            val startDate: LocalDate,
            val endDate: LocalDate?
        ) : DateContent

    }
}