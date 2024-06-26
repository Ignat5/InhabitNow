package com.example.inhabitnow.android.presentation.view_schedule.components

import androidx.compose.runtime.Stable
import com.example.inhabitnow.android.presentation.base.components.state.ScreenState
import com.example.inhabitnow.android.presentation.model.UIResultModel
import com.example.inhabitnow.android.presentation.view_schedule.model.FullTaskWithRecordModel
import com.example.inhabitnow.android.presentation.view_schedule.model.ItemDayOfWeek
import kotlinx.datetime.LocalDate

@Stable
data class ViewScheduleScreenState(
    val allTasksWithRecord: UIResultModel<List<FullTaskWithRecordModel>>,
    val currentDate: LocalDate,
    val startOfWeekDate: LocalDate,
    val todayDate: LocalDate,
    val isLocked: Boolean
) : ScreenState
