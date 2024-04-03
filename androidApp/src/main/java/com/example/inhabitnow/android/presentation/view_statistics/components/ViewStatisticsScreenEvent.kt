package com.example.inhabitnow.android.presentation.view_statistics.components

import com.example.inhabitnow.android.presentation.base.components.event.ScreenEvent

sealed interface ViewStatisticsScreenEvent : ScreenEvent {
    data object OnDismissRequest : ViewStatisticsScreenEvent
}