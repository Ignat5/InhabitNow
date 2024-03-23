package com.example.inhabitnow.android.presentation.create_edit_task.edit.model

sealed interface ItemTaskAction {
    data object ViewStatistics : ItemTaskAction
    data object RestartHabit : ItemTaskAction
    data object DeleteTask : ItemTaskAction
    sealed interface ArchiveUnarchive : ItemTaskAction {
        data object Archive : ArchiveUnarchive
        data object Unarchive : ArchiveUnarchive
    }
}