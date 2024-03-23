package com.example.inhabitnow.android.presentation.create_edit_task.common.config.model

import com.example.inhabitnow.android.presentation.model.UITaskContent
import kotlinx.datetime.LocalDate

sealed class BaseItemTaskConfig(val key: Key, val contentType: ContentType) {
    enum class Key { Title, Description, Progress, Frequency, OneDayDate, StartDate, EndDate, Reminders, Tags, Priority }
    enum class ContentType { Basic, Switch }

    data class Title(val title: String) : BaseItemTaskConfig(Key.Title, ContentType.Basic)
    data class Description(
        val description: String
    ) : BaseItemTaskConfig(Key.Description, ContentType.Basic)

    data class Priority(val priority: String) : BaseItemTaskConfig(Key.Priority, ContentType.Basic)
    data class Frequency(
        val uiFrequencyContent: UITaskContent.Frequency
    ) : BaseItemTaskConfig(Key.Frequency, ContentType.Basic)

    data class Reminders(val count: Int) : BaseItemTaskConfig(Key.Reminders, ContentType.Basic)
    data class Tags(val count: Int) : BaseItemTaskConfig(Key.Tags, ContentType.Basic)

    sealed class Date(key: Key, contentType: ContentType) : BaseItemTaskConfig(key, contentType) {
        abstract val date: LocalDate?

        data class OneDayDate(
            override val date: LocalDate
        ) : Date(Key.OneDayDate, ContentType.Basic)

        data class StartDate(
            override val date: LocalDate
        ) : Date(Key.StartDate, ContentType.Basic)

        data class EndDate(
            override val date: LocalDate?
        ) : Date(Key.EndDate, ContentType.Switch)
    }

    sealed class Progress(key: Key, contentType: ContentType) : BaseItemTaskConfig(key, contentType) {
        abstract val uiProgressContent: UITaskContent.Progress

        data class Number(
            override val uiProgressContent: UITaskContent.Progress.Number
        ) : Progress(Key.Progress, ContentType.Basic)

        data class Time(
            override val uiProgressContent: UITaskContent.Progress.Time
        ) : Progress(Key.Progress, ContentType.Basic)
    }
}