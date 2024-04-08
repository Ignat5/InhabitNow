package com.example.inhabitnow.android.presentation.view_activities.view_tasks.components

import com.example.inhabitnow.android.presentation.base.components.event.ScreenEvent
import com.example.inhabitnow.android.presentation.base.components.result.ScreenResult
import com.example.inhabitnow.android.presentation.main.config.pick_task_type.PickTaskTypeScreenResult
import com.example.inhabitnow.android.presentation.view_activities.base.components.BaseViewTasksScreenEvent
import com.example.inhabitnow.android.presentation.view_activities.model.TaskFilterByStatus
import com.example.inhabitnow.android.presentation.view_activities.model.TaskSort

sealed interface ViewTasksScreenEvent : ScreenEvent {
    data class Base(val baseEvent: BaseViewTasksScreenEvent) : ViewTasksScreenEvent
    data class OnTaskClick(val taskId: String) : ViewTasksScreenEvent
    data class OnFilterByStatusClick(
        val filter: TaskFilterByStatus.TaskStatus
    ) : ViewTasksScreenEvent

    data class OnSortClick(val sort: TaskSort.TasksSort) : ViewTasksScreenEvent
    data object OnCreateTaskClick : ViewTasksScreenEvent

    sealed interface ResultEvent : ViewTasksScreenEvent {
        val result: ScreenResult

        data class PickTaskType(
            override val result: PickTaskTypeScreenResult
        ) : ResultEvent
    }
}