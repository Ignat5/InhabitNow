package com.example.inhabitnow.android.presentation.view_activities.view_habits.components

import com.example.inhabitnow.android.presentation.base.components.event.ScreenEvent
import com.example.inhabitnow.android.presentation.view_activities.model.TaskFilterByStatus

sealed interface ViewHabitsScreenEvent : ScreenEvent {
    data class OnHabitClick(val taskId: String) : ViewHabitsScreenEvent
    data class OnFilterTagClick(val tagId: String) : ViewHabitsScreenEvent
    data class OnFilterByStatusClick(val filterByStatus: TaskFilterByStatus.HabitStatus) : ViewHabitsScreenEvent
    data object OnSearchTasksClick : ViewHabitsScreenEvent
}