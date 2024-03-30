package com.example.inhabitnow.android.presentation.view_activities.view_habits.components

import com.example.inhabitnow.android.presentation.base.components.navigation.ScreenNavigation

sealed interface ViewHabitsScreenNavigation : ScreenNavigation {
    data object Search : ViewHabitsScreenNavigation
    data class EditTask(val taskId: String) : ViewHabitsScreenNavigation
}