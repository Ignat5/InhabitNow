package com.example.inhabitnow.android.presentation.view_schedule.model

import kotlinx.datetime.LocalDate

sealed interface ItemDayOfWeek {
    val date: LocalDate

    data class Day(override val date: LocalDate) : ItemDayOfWeek
    data class Today(override val date: LocalDate) : ItemDayOfWeek
    data class Current(override val date: LocalDate) : ItemDayOfWeek
}