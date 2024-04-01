package com.example.inhabitnow.android.presentation.view_activities.view_habits.config.view_habit_actions.components

import com.example.inhabitnow.android.presentation.base.components.event.ScreenEvent
import com.example.inhabitnow.android.presentation.view_activities.model.ItemTaskAction

sealed interface ViewHabitActionsScreenEvent : ScreenEvent {
    data object OnEditClick : ViewHabitActionsScreenEvent
    data class OnItemActionClick(
        val itemTaskAction: ItemTaskAction.HabitAction
    ) : ViewHabitActionsScreenEvent

    data object OnDismissRequest : ViewHabitActionsScreenEvent
}