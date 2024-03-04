package com.example.inhabitnow.domain.model.task.content

import com.example.inhabitnow.core.type.ProgressLimitType
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalTime

sealed interface TaskContentModel {

    sealed interface ProgressContent : TaskContentModel {
        data object YesNo : ProgressContent

        data class Number(
            val limitType: ProgressLimitType,
            val limitNumber: String,
            val limitUnit: String
        ) : ProgressContent

        data class Time(
            val limitType: ProgressLimitType,
            val limitTime: LocalTime
        ) : ProgressContent
    }

    sealed interface FrequencyContent : TaskContentModel {
        data object OneDay : FrequencyContent
        sealed class Period(val periodType: PeriodType) : FrequencyContent {
            enum class PeriodType { EveryDay, DaysOfWeek }
            data object EveryDay : Period(PeriodType.EveryDay)
            data class DaysOfWeek(val daysOfWeek: Set<DayOfWeek>) : Period(PeriodType.DaysOfWeek)
        }
    }

    data class ArchiveContent(val isArchived: Boolean) : TaskContentModel
}