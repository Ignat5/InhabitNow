package com.example.inhabitnow.android.presentation.view_activities.model

sealed interface ItemTaskAction {
    data object ViewStatistics : HabitAction
    data object Archive : HabitAction, TaskAction
    data object Delete : HabitAction, TaskAction

    sealed interface HabitAction : ItemTaskAction
    sealed interface TaskAction : ItemTaskAction
}