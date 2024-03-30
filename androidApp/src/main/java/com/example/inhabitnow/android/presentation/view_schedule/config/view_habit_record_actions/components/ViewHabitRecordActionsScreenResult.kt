package com.example.inhabitnow.android.presentation.view_schedule.config.view_habit_record_actions.components

import com.example.inhabitnow.android.presentation.base.components.result.ScreenResult
import kotlinx.datetime.LocalDate

sealed interface ViewHabitRecordActionsScreenResult : ScreenResult {
    data class Confirm(
        val taskId: String,
        val date: LocalDate,
        val action: Action
    ) : ViewHabitRecordActionsScreenResult

    data object Dismiss : ViewHabitRecordActionsScreenResult

    sealed interface Action {
        data object Edit : Action
        data object EnterRecord : Action
        data object Fail : Action
        data object Skip : Action
        data object ResetEntry : Action
    }
}