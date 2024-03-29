package com.example.inhabitnow.android.presentation.view_schedule.components

import com.example.inhabitnow.android.presentation.base.components.event.ScreenEvent
import com.example.inhabitnow.android.presentation.base.components.result.ScreenResult
import com.example.inhabitnow.android.presentation.common.pick_date.components.PickDateScreenResult
import com.example.inhabitnow.android.presentation.view_schedule.config.enter_number_record.components.EnterTaskNumberRecordScreenResult
import com.example.inhabitnow.android.presentation.view_schedule.config.enter_time_record.components.EnterTaskTimeRecordScreenResult
import com.example.inhabitnow.android.presentation.view_schedule.config.view_habit_record_actions.components.ViewHabitRecordActionsScreenResult
import kotlinx.datetime.LocalDate

sealed interface ViewScheduleScreenEvent : ScreenEvent {
    data class OnTaskClick(val taskId: String) : ViewScheduleScreenEvent
    data class OnTaskLongClick(val taskId: String) : ViewScheduleScreenEvent
    data class OnDateClick(val date: LocalDate) : ViewScheduleScreenEvent
    data object OnPrevWeekClick : ViewScheduleScreenEvent
    data object OnNextWeekClick : ViewScheduleScreenEvent
    data object OnSearchClick : ViewScheduleScreenEvent
    data object OnPickDateClick : ViewScheduleScreenEvent

    sealed interface ResultEvent : ViewScheduleScreenEvent {
        val result: ScreenResult

        data class PickDate(override val result: PickDateScreenResult) : ResultEvent
        data class EnterTaskNumberRecord(
            override val result: EnterTaskNumberRecordScreenResult
        ) : ResultEvent

        data class EnterTaskTimeRecord(
            override val result: EnterTaskTimeRecordScreenResult
        ) : ResultEvent

        data class ViewHabitRecordActions(
            override val result: ViewHabitRecordActionsScreenResult
        ) : ResultEvent
    }
}