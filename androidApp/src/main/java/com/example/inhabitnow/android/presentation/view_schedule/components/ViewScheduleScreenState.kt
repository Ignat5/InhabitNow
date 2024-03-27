package com.example.inhabitnow.android.presentation.view_schedule.components

import com.example.inhabitnow.android.presentation.base.components.state.ScreenState
import com.example.inhabitnow.android.presentation.view_schedule.model.FullTaskWithRecordModel
import kotlinx.datetime.LocalDate

data class ViewScheduleScreenState(
    val currentDate: LocalDate,
    val allTasksWithRecord: List<FullTaskWithRecordModel>,
    val isLocked: Boolean
) : ScreenState
