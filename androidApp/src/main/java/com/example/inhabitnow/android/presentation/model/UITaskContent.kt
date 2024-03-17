package com.example.inhabitnow.android.presentation.model

import com.example.inhabitnow.domain.model.task.content.TaskContentModel
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate

sealed interface UITaskContent {
    sealed interface Progress : UITaskContent {
        val progressContent: TaskContentModel.ProgressContent

        data class Number(
            override val progressContent: TaskContentModel.ProgressContent.Number
        ) : Progress

        data class Time(
            override val progressContent: TaskContentModel.ProgressContent.Time
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