package com.example.inhabitnow.android.presentation.view_schedule.components

import com.example.inhabitnow.android.presentation.base.components.config.ScreenConfig
import com.example.inhabitnow.android.presentation.common.pick_date.PickDateStateHolder
import com.example.inhabitnow.android.presentation.view_schedule.config.enter_number_record.EnterTaskNumberRecordStateHolder

sealed interface ViewScheduleScreenConfig : ScreenConfig {
    data class PickDate(
        val stateHolder: PickDateStateHolder
    ) : ViewScheduleScreenConfig

    data class EnterTaskNumberRecord(
        val stateHolder: EnterTaskNumberRecordStateHolder
    ) : ViewScheduleScreenConfig
}