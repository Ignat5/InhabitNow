package com.example.inhabitnow.android.presentation.view_schedule.config.enter_time_record.components

import com.example.inhabitnow.android.presentation.base.components.result.ScreenResult
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime

sealed interface EnterTaskTimeRecordScreenResult : ScreenResult {
    data class Confirm(
        val taskId: String,
        val date: LocalDate,
        val time: LocalTime
    ) : EnterTaskTimeRecordScreenResult
    data object Dismiss : EnterTaskTimeRecordScreenResult
}