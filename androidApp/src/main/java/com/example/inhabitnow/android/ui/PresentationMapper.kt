package com.example.inhabitnow.android.ui

import com.example.inhabitnow.android.presentation.model.SelectableDayOfWeek
import com.example.inhabitnow.android.presentation.model.UITaskContent
import com.example.inhabitnow.domain.model.task.content.TaskContentModel
import kotlinx.datetime.DayOfWeek

fun TaskContentModel.FrequencyContent.toUIFrequencyContent(): UITaskContent.Frequency? {
    return when (this) {
        is TaskContentModel.FrequencyContent.EveryDay -> UITaskContent.Frequency.EveryDay
        is TaskContentModel.FrequencyContent.DaysOfWeek ->
            UITaskContent.Frequency.DaysOfWeek(this.daysOfWeek)

        else -> null
    }
}

fun UITaskContent.Frequency.toFrequencyContent(): TaskContentModel.FrequencyContent {
    return when (this) {
        is UITaskContent.Frequency.EveryDay -> TaskContentModel.FrequencyContent.EveryDay
        is UITaskContent.Frequency.DaysOfWeek -> TaskContentModel.FrequencyContent.DaysOfWeek(this.daysOfWeek)
    }
}