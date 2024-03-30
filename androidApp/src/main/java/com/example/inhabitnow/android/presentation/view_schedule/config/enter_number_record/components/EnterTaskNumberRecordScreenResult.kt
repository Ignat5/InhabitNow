package com.example.inhabitnow.android.presentation.view_schedule.config.enter_number_record.components

import com.example.inhabitnow.android.presentation.base.components.result.ScreenResult
import kotlinx.datetime.LocalDate

sealed interface EnterTaskNumberRecordScreenResult : ScreenResult {
    data class Confirm(
        val taskId: String,
        val number: Double,
        val date: LocalDate
    ) : EnterTaskNumberRecordScreenResult

    data object Dismiss : EnterTaskNumberRecordScreenResult
}