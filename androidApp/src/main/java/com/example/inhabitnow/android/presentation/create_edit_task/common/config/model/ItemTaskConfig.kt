package com.example.inhabitnow.android.presentation.create_edit_task.common.config.model

import com.example.inhabitnow.android.presentation.model.UITaskContent
import kotlinx.datetime.LocalDate

sealed class ItemTaskConfig(val key: Key, val contentType: ContentType) {
    enum class Key { Title, Description, Progress, Frequency, OneDayDate, StartDate, EndDate, Reminders, Tags, Priority }
    enum class ContentType { Basic, Switch }

    data class Title(val title: String) : ItemTaskConfig(Key.Title, ContentType.Basic)
    data class Description(
        val description: String
    ) : ItemTaskConfig(Key.Description, ContentType.Basic)

    data class Priority(val priority: String) : ItemTaskConfig(Key.Priority, ContentType.Basic)
    data class Frequency(
        val uiFrequencyContent: UITaskContent.Frequency
    ) : ItemTaskConfig(Key.Frequency, ContentType.Basic)

    data class Reminders(val count: Int) : ItemTaskConfig(Key.Reminders, ContentType.Basic)
    data class Tags(val count: Int) : ItemTaskConfig(Key.Tags, ContentType.Basic)

    sealed class Date(key: Key, contentType: ContentType) : ItemTaskConfig(key, contentType) {
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

    sealed class Progress(key: Key, contentType: ContentType) : ItemTaskConfig(key, contentType) {
        abstract val uiProgressContent: UITaskContent.Progress

        data class Number(
            override val uiProgressContent: UITaskContent.Progress.Number
        ) : Progress(Key.Progress, ContentType.Basic)

        data class Time(
            override val uiProgressContent: UITaskContent.Progress.Time
        ) : Progress(Key.Progress, ContentType.Basic)
    }
}