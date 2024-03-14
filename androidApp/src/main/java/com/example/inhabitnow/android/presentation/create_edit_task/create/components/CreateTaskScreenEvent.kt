package com.example.inhabitnow.android.presentation.create_edit_task.create.components

import com.example.inhabitnow.android.presentation.base.components.event.ScreenEvent
import com.example.inhabitnow.android.presentation.base.components.result.ScreenResult
import com.example.inhabitnow.android.presentation.create_edit_task.common.config.pick_task_title.components.PickTaskTitleScreenResult

sealed interface CreateTaskScreenEvent : ScreenEvent {
    data object OnSaveClick : CreateTaskScreenEvent
    data object OnDismissRequest : CreateTaskScreenEvent

    sealed interface ConfigEvent : CreateTaskScreenEvent {
        data object OnConfigTaskTitleClick : ConfigEvent
    }

    sealed interface ResultEvent : CreateTaskScreenEvent {
        val result: ScreenResult

        data class PickTaskTitle(override val result: PickTaskTitleScreenResult) : ResultEvent
    }
}