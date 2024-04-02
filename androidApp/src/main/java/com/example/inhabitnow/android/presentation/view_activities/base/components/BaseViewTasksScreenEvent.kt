package com.example.inhabitnow.android.presentation.view_activities.base.components

import com.example.inhabitnow.android.presentation.base.components.event.ScreenEvent
import com.example.inhabitnow.android.presentation.base.components.result.ScreenResult
import com.example.inhabitnow.android.presentation.create_edit_task.edit.config.confirm_archive.ConfirmArchiveTaskScreenResult
import com.example.inhabitnow.android.presentation.create_edit_task.edit.config.confirm_delete.ConfirmDeleteTaskScreenResult

sealed interface BaseViewTasksScreenEvent : ScreenEvent {
    data class OnTagClick(val tagId: String) : BaseViewTasksScreenEvent
    data object OnSearchClick : BaseViewTasksScreenEvent

    sealed interface ResultEvent : BaseViewTasksScreenEvent {
        val result: ScreenResult

        data class ConfirmArchiveTask(
            override val result: ConfirmArchiveTaskScreenResult
        ) : ResultEvent

        data class ConfirmDeleteTask(
            override val result: ConfirmDeleteTaskScreenResult
        ) : ResultEvent

    }
}