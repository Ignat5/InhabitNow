package com.example.inhabitnow.android.presentation.model

import kotlinx.datetime.DayOfWeek

sealed interface UIReminderContent {
    sealed class Schedule(val type: Type) : UIReminderContent {
        enum class Type { EveryDay, DaysOfWeek }
        data object EveryDay : Schedule(Type.EveryDay)
        data class DaysOfWeek(val daysOfWeek: Set<DayOfWeek>) : Schedule(Type.DaysOfWeek)
    }

}