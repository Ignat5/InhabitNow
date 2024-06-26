package com.example.inhabitnow.android.presentation.create_edit_task.edit.components

import com.example.inhabitnow.android.presentation.base.components.event.ScreenEvent
import com.example.inhabitnow.android.presentation.base.components.result.ScreenResult
import com.example.inhabitnow.android.presentation.create_edit_task.base.components.BaseCreateEditTaskScreenEvent
import com.example.inhabitnow.android.presentation.create_edit_task.edit.config.confirm_archive.ConfirmArchiveTaskScreenResult
import com.example.inhabitnow.android.presentation.create_edit_task.edit.config.confirm_delete.ConfirmDeleteTaskScreenResult
import com.example.inhabitnow.android.presentation.create_edit_task.edit.config.confirm_restart.ConfirmRestartHabitScreenResult
import com.example.inhabitnow.android.presentation.create_edit_task.edit.model.ItemTaskAction

sealed interface EditTaskScreenEvent : ScreenEvent {
    data class BaseEvent(val baseEvent: BaseCreateEditTaskScreenEvent) : EditTaskScreenEvent
    data class OnItemTaskActionClick(val item: ItemTaskAction) : EditTaskScreenEvent
    data object OnBackRequest : EditTaskScreenEvent

    sealed interface ResultEvent : EditTaskScreenEvent {
        val result: ScreenResult

        data class ConfirmArchiveTask(
            override val result: ConfirmArchiveTaskScreenResult
        ) : ResultEvent

        data class ConfirmDeleteTask(
            override val result: ConfirmDeleteTaskScreenResult
        ) : ResultEvent

        data class ConfirmRestartHabit(
            override val result: ConfirmRestartHabitScreenResult
        ) : ResultEvent
    }
}