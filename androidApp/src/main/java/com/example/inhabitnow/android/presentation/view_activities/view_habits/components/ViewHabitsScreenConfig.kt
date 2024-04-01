package com.example.inhabitnow.android.presentation.view_activities.view_habits.components

import com.example.inhabitnow.android.presentation.base.components.config.ScreenConfig
import com.example.inhabitnow.android.presentation.view_activities.view_habits.config.view_habit_actions.ViewHabitActionsStateHolder

sealed interface ViewHabitsScreenConfig : ScreenConfig {
    data class ViewHabitActions(
        val stateHolder: ViewHabitActionsStateHolder
    ) : ViewHabitsScreenConfig

    data class ConfirmArchiveTask(
        val taskId: String
    ) : ViewHabitsScreenConfig

    data class ConfirmDeleteTask(
        val taskId: String
    ) : ViewHabitsScreenConfig
}