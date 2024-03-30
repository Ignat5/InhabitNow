package com.example.inhabitnow.android.presentation.view_schedule.config.enter_number_record.components

import com.example.inhabitnow.android.presentation.base.components.event.ScreenEvent

sealed interface EnterTaskNumberRecordScreenEvent : ScreenEvent {
    data class InputUpdateNumber(val value: String) : EnterTaskNumberRecordScreenEvent
    data object OnConfirmClick : EnterTaskNumberRecordScreenEvent
    data object OnDismissRequest : EnterTaskNumberRecordScreenEvent
}