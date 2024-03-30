package com.example.inhabitnow.data.model.task.content

import com.example.inhabitnow.core.type.ProgressLimitType
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@SerialName("TaskContent")
@Serializable
sealed interface TaskContentEntity {

    enum class Type { Progress, Frequency, Archive }

    @SerialName("TaskContent.ProgressContent")
    @Serializable
    sealed class ProgressContent : TaskContentEntity {

        @SerialName("TaskContent.ProgressContent.YesNo")
        @Serializable
        data object YesNo : ProgressContent()

        @SerialName("TaskContent.ProgressContent.Number")
        @Serializable
        data class Number(
            val limitType: ProgressLimitType,
            val limitNumber: Double,
            val limitUnit: String
        ) : ProgressContent()

        @SerialName("TaskContent.ProgressContent.Time")
        @Serializable
        data class Time(
            val limitType: ProgressLimitType,
            val limitTime: LocalTime
        ) : ProgressContent()

    }

    @SerialName("TaskContent.FrequencyContent")
    @Serializable
    sealed class FrequencyContent : TaskContentEntity {
        @SerialName("TaskContent.FrequencyContent.OneDay")
        @Serializable
        data object OneDay : FrequencyContent()

        @SerialName("TaskContent.FrequencyContent.EveryDay")
        @Serializable
        data object EveryDay : FrequencyContent()

        @SerialName("TaskContent.FrequencyContent.DaysOfWeek")
        @Serializable
        data class DaysOfWeek(val daysOfWeek: Set<DayOfWeek>) : FrequencyContent()
    }

    @SerialName("TaskContent.ArchiveContent")
    @Serializable
    data class ArchiveContent(val isArchived: Boolean) : TaskContentEntity

}