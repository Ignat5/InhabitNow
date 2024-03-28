package com.example.inhabitnow.android.presentation.view_schedule.components

import com.example.inhabitnow.android.presentation.base.components.event.ScreenEvent
import com.example.inhabitnow.android.presentation.base.components.result.ScreenResult
import com.example.inhabitnow.android.presentation.common.pick_date.components.PickDateScreenResult
import kotlinx.datetime.LocalDate

sealed interface ViewScheduleScreenEvent : ScreenEvent {
    data class OnDateClick(val date: LocalDate) : ViewScheduleScreenEvent
    data object OnPrevWeekClick : ViewScheduleScreenEvent
    data object OnNextWeekClick : ViewScheduleScreenEvent
    data object OnSearchClick : ViewScheduleScreenEvent
    data object OnPickDateClick : ViewScheduleScreenEvent

    sealed interface ResultEvent : ViewScheduleScreenEvent {
        val result: ScreenResult

        data class PickDate(override val result: PickDateScreenResult) : ResultEvent
    }
}