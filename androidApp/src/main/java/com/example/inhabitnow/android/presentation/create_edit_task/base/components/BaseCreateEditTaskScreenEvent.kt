package com.example.inhabitnow.android.presentation.create_edit_task.base.components

import com.example.inhabitnow.android.presentation.base.components.event.ScreenEvent
import com.example.inhabitnow.android.presentation.base.components.result.ScreenResult
import com.example.inhabitnow.android.presentation.create_edit_task.common.config.model.BaseItemTaskConfig
import com.example.inhabitnow.android.presentation.create_edit_task.common.config.pick_description.components.PickTaskDescriptionScreenResult
import com.example.inhabitnow.android.presentation.create_edit_task.common.config.pick_task_title.components.PickTaskTitleScreenResult

sealed interface BaseCreateEditTaskScreenEvent : ScreenEvent {
    data class OnBaseItemTaskConfigClick(
        val item: BaseItemTaskConfig
    ) : BaseCreateEditTaskScreenEvent

    sealed interface ResultEvent : BaseCreateEditTaskScreenEvent {
        val result: ScreenResult

        data class PickTaskTitle(
            override val result: PickTaskTitleScreenResult
        ) : ResultEvent

        data class PickTaskDescription(
            override val result: PickTaskDescriptionScreenResult
        ) : ResultEvent
    }
}