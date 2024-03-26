package com.example.inhabitnow.android.presentation.model

import com.example.inhabitnow.core.type.ProgressLimitType
import com.example.inhabitnow.domain.model.task.content.TaskContentModel
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime

sealed interface UITaskContent {
    sealed interface Progress : UITaskContent {

        data class Number(
            val limitType: ProgressLimitType,
            val limitNumber: Double,
            val limitUnit: String
        ) : Progress

        data class Time(
            val limitType: ProgressLimitType,
            val limitTime: LocalTime
        ) : Progress
    }

    sealed class Frequency(val type: Type) : UITaskContent {
        enum class Type { EveryDay, DaysOfWeek }

        data object EveryDay : Frequency(Type.EveryDay)

        data class DaysOfWeek(val daysOfWeek: Set<DayOfWeek>) : Frequency(Type.DaysOfWeek)
    }

    sealed interface Date : UITaskContent {
        data class OneDay(val date: LocalDate) : Date
        data class Period(val startDate: LocalDate, val endDate: LocalDate?) : Date
    }
}