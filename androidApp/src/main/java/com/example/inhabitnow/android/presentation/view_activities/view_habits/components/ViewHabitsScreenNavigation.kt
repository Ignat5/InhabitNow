package com.example.inhabitnow.android.presentation.view_activities.view_habits.components

import com.example.inhabitnow.android.presentation.base.components.navigation.ScreenNavigation
import com.example.inhabitnow.android.presentation.view_activities.base.components.BaseViewTasksScreenNavigation

sealed interface ViewHabitsScreenNavigation : ScreenNavigation {
    data class Base(val baseNS: BaseViewTasksScreenNavigation) : ViewHabitsScreenNavigation
    data class ViewStatistics(val taskId: String) : ViewHabitsScreenNavigation
    data class CreateTask(val taskId: String) : ViewHabitsScreenNavigation
}