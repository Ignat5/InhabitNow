package com.example.inhabitnow.android.presentation.search_tasks.components

import com.example.inhabitnow.android.presentation.base.components.event.ScreenEvent

sealed interface SearchTasksScreenEvent : ScreenEvent {
    data class OnInputUpdateSearchQuery(val value: String) : SearchTasksScreenEvent
    data class OnTaskClick(val taskId: String) : SearchTasksScreenEvent
    data object OnClearSearchClick : SearchTasksScreenEvent
    data object OnBackRequest : SearchTasksScreenEvent
}