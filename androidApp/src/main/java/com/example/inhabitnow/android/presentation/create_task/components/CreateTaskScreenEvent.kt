package com.example.inhabitnow.android.presentation.create_task.components

import com.example.inhabitnow.android.presentation.base.components.event.ScreenEvent

sealed interface CreateTaskScreenEvent: ScreenEvent {
    data object OnSaveClick : CreateTaskScreenEvent
    data object OnDismissRequest : CreateTaskScreenEvent
}