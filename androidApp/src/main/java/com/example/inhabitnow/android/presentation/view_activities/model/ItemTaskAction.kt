package com.example.inhabitnow.android.presentation.view_activities.model

sealed interface ItemTaskAction {
    data object ViewStatistics : HabitAction
    sealed interface ArchiveUnarchive : HabitAction, TaskAction {
        data object Archive : ArchiveUnarchive
        data object Unarchive : ArchiveUnarchive
    }

    data object Delete : HabitAction, TaskAction


    sealed interface HabitAction : ItemTaskAction
    sealed interface TaskAction : ItemTaskAction
}