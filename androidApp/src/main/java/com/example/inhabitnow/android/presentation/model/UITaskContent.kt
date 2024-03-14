package com.example.inhabitnow.android.presentation.model

import com.example.inhabitnow.domain.model.task.content.TaskContentModel
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

    sealed interface Frequency : UITaskContent {
        val frequencyContent: TaskContentModel.FrequencyContent

        data class EveryDay(
            override val frequencyContent: TaskContentModel.FrequencyContent.EveryDay
        ) : Frequency

        data class DaysOfWeek(
            override val frequencyContent: TaskContentModel.FrequencyContent.DaysOfWeek
        ) : Frequency
    }

    sealed interface Date : UITaskContent {
        data class OneDay(val date: LocalDate) : Date
        data class Period(val startDate: LocalDate, val endDate: LocalDate?) : Date
    }
}