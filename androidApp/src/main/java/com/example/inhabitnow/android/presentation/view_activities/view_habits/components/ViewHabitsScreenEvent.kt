package com.example.inhabitnow.android.presentation.view_activities.view_habits.components

import com.example.inhabitnow.android.presentation.base.components.event.ScreenEvent
import com.example.inhabitnow.android.presentation.base.components.result.ScreenResult
import com.example.inhabitnow.android.presentation.create_edit_task.edit.config.confirm_archive.ConfirmArchiveTaskScreenResult
import com.example.inhabitnow.android.presentation.create_edit_task.edit.config.confirm_delete.ConfirmDeleteTaskScreenResult
import com.example.inhabitnow.android.presentation.view_activities.base.components.BaseViewTasksScreenEvent
import com.example.inhabitnow.android.presentation.view_activities.model.TaskFilterByStatus
import com.example.inhabitnow.android.presentation.view_activities.model.TaskSort
import com.example.inhabitnow.android.presentation.view_activities.view_habits.config.view_habit_actions.components.ViewHabitActionsScreenResult

sealed interface ViewHabitsScreenEvent : ScreenEvent {
    data class Base(val baseEvent: BaseViewTasksScreenEvent) : ViewHabitsScreenEvent
    data class OnHabitClick(val taskId: String) : ViewHabitsScreenEvent
    data class OnFilterByStatusClick(val filterByStatus: TaskFilterByStatus.HabitStatus) :
        ViewHabitsScreenEvent

    data class OnSortClick(val sort: TaskSort.HabitsSort) : ViewHabitsScreenEvent

    sealed interface ResultEvent : ViewHabitsScreenEvent {
        val result: ScreenResult

        data class ViewHabitActions(
            override val result: ViewHabitActionsScreenResult
        ) : ResultEvent

    }
}