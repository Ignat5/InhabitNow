package com.example.inhabitnow.android.presentation.view_schedule.config.view_habit_record_actions.components

import com.example.inhabitnow.android.presentation.base.components.event.ScreenEvent
import com.example.inhabitnow.android.presentation.view_schedule.config.view_habit_record_actions.model.ItemHabitRecordAction

sealed interface ViewHabitRecordActionsScreenEvent : ScreenEvent {
    data class OnActionClick(val action: ItemHabitRecordAction) : ViewHabitRecordActionsScreenEvent
    data object OnEditClick : ViewHabitRecordActionsScreenEvent
    data object OnDismissRequest : ViewHabitRecordActionsScreenEvent
}