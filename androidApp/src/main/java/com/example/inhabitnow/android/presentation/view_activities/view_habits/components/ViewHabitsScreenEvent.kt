package com.example.inhabitnow.android.presentation.view_activities.view_habits.components

import com.example.inhabitnow.android.presentation.base.components.event.ScreenEvent

sealed interface ViewHabitsScreenEvent : ScreenEvent {
    data class OnHabitClick(val taskId: String) : ViewHabitsScreenEvent
    data object OnSearchTasksClick : ViewHabitsScreenEvent
}