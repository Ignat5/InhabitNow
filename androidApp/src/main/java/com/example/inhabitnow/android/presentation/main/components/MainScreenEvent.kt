package com.example.inhabitnow.android.presentation.main.components

import com.example.inhabitnow.android.presentation.base.components.event.ScreenEvent

sealed interface MainScreenEvent : ScreenEvent {
    data object OnCreateTaskClick : MainScreenEvent
}