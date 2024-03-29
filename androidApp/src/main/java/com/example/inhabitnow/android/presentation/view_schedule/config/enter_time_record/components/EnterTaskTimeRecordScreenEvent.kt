package com.example.inhabitnow.android.presentation.view_schedule.config.enter_time_record.components

import com.example.inhabitnow.android.presentation.base.components.event.ScreenEvent

sealed interface EnterTaskTimeRecordScreenEvent : ScreenEvent {
    data class OnInputUpdateHours(val hours: Int) : EnterTaskTimeRecordScreenEvent
    data class OnInputUpdateMinutes(val minutes: Int) : EnterTaskTimeRecordScreenEvent
    data object OnConfirmClick : EnterTaskTimeRecordScreenEvent
    data object OnDismissRequest : EnterTaskTimeRecordScreenEvent
}