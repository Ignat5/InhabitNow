package com.example.inhabitnow.android.presentation.view_activities.view_habits.components

import com.example.inhabitnow.android.presentation.base.components.config.ScreenConfig
import com.example.inhabitnow.android.presentation.view_activities.base.components.BaseViewTasksScreenConfig
import com.example.inhabitnow.android.presentation.view_activities.view_habits.config.view_habit_actions.ViewHabitActionsStateHolder

sealed interface ViewHabitsScreenConfig : ScreenConfig {
    data class Base(val baseConfig: BaseViewTasksScreenConfig) : ViewHabitsScreenConfig

    data class ViewHabitActions(
        val stateHolder: ViewHabitActionsStateHolder
    ) : ViewHabitsScreenConfig
}