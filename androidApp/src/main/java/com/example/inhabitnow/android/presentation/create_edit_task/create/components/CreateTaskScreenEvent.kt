package com.example.inhabitnow.android.presentation.create_edit_task.create.components

import com.example.inhabitnow.android.presentation.base.components.event.ScreenEvent
import com.example.inhabitnow.android.presentation.base.components.result.ScreenResult
import com.example.inhabitnow.android.presentation.create_edit_task.common.config.pick_frequency.components.PickTaskFrequencyScreenResult
import com.example.inhabitnow.android.presentation.create_edit_task.common.config.pick_task_title.components.PickTaskTitleScreenResult
import com.example.inhabitnow.android.presentation.create_edit_task.common.config.pick_progress.number.components.PickTaskNumberProgressScreenResult
import com.example.inhabitnow.android.presentation.create_edit_task.common.config.pick_progress.time.components.PickTaskTimeProgressScreenResult

sealed interface CreateTaskScreenEvent : ScreenEvent {
    data object OnSaveClick : CreateTaskScreenEvent
    data object OnDismissRequest : CreateTaskScreenEvent

    sealed interface ConfigEvent : CreateTaskScreenEvent {
        data object OnConfigTaskTitleClick : ConfigEvent
        data object OnConfigTaskNumberProgressClick : ConfigEvent
        data object OnConfigTaskTimeProgressClick : ConfigEvent
        data object OnConfigTaskFrequencyClick : ConfigEvent
    }

    sealed interface ResultEvent : CreateTaskScreenEvent {
        val result: ScreenResult

        data class PickTaskTitle(
            override val result: PickTaskTitleScreenResult
        ) : ResultEvent

        data class PickTaskNumberProgress(
            override val result: PickTaskNumberProgressScreenResult
        ) : ResultEvent

        data class PickTaskTimeProgress(
            override val result: PickTaskTimeProgressScreenResult
        ) : ResultEvent

        data class PickTaskFrequency(
            override val result: PickTaskFrequencyScreenResult
        ) : ResultEvent
    }
}