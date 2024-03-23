package com.example.inhabitnow.android.presentation.main.components

import com.example.inhabitnow.android.presentation.base.components.event.ScreenEvent
import com.example.inhabitnow.android.presentation.base.components.result.ScreenResult
import com.example.inhabitnow.android.presentation.main.config.pick_task_progress_type.PickTaskProgressTypeScreenResult
import com.example.inhabitnow.android.presentation.main.config.pick_task_type.PickTaskTypeScreenResult

sealed interface MainScreenEvent : ScreenEvent {
    data object OnCreateTaskClick : MainScreenEvent
    data object OnSearchTasksClick : MainScreenEvent

    sealed interface ResultEvent : MainScreenEvent {
        val result: ScreenResult

        data class PickTaskType(
            override val result: PickTaskTypeScreenResult
        ) : ResultEvent

        data class PickTaskProgressType(
            override val result: PickTaskProgressTypeScreenResult
        ) : ResultEvent
    }
}