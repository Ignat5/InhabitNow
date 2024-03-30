package com.example.inhabitnow.android.presentation.view_schedule.components

import com.example.inhabitnow.android.presentation.base.components.config.ScreenConfig
import com.example.inhabitnow.android.presentation.common.pick_date.PickDateStateHolder
import com.example.inhabitnow.android.presentation.view_schedule.config.enter_number_record.EnterTaskNumberRecordStateHolder
import com.example.inhabitnow.android.presentation.view_schedule.config.enter_time_record.EnterTaskTimeRecordStateHolder
import com.example.inhabitnow.android.presentation.view_schedule.config.view_habit_record_actions.ViewHabitRecordActionsStateHolder

sealed interface ViewScheduleScreenConfig : ScreenConfig {
    data class PickDate(
        val stateHolder: PickDateStateHolder
    ) : ViewScheduleScreenConfig

    data class EnterTaskNumberRecord(
        val stateHolder: EnterTaskNumberRecordStateHolder
    ) : ViewScheduleScreenConfig

    data class EnterTaskTimeRecord(
        val stateHolder: EnterTaskTimeRecordStateHolder
    ) : ViewScheduleScreenConfig

    data class ViewHabitRecordActions(
        val stateHolder: ViewHabitRecordActionsStateHolder
    ) : ViewScheduleScreenConfig
}