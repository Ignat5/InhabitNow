package com.example.inhabitnow.android.presentation.view_activities.base.components

import com.example.inhabitnow.android.presentation.base.components.navigation.ScreenNavigation

sealed interface BaseViewTasksScreenNavigation : ScreenNavigation {
    data object Search : BaseViewTasksScreenNavigation
    data class EditTask(val taskId: String) : BaseViewTasksScreenNavigation
}