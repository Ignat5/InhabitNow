package com.example.inhabitnow.android.presentation.create_edit_task.common.config.pick_frequency.components

import com.example.inhabitnow.android.presentation.base.components.event.ScreenEvent
import com.example.inhabitnow.android.presentation.model.UITaskContent
import kotlinx.datetime.DayOfWeek

sealed interface PickTaskFrequencyScreenEvent : ScreenEvent {
    data class OnFrequencyTypeClick(
        val type: UITaskContent.Frequency.Type
    ) : PickTaskFrequencyScreenEvent

    data class OnDayOfWeekClick(val dayOfWeek: DayOfWeek) : PickTaskFrequencyScreenEvent

    data object OnConfirmClick : PickTaskFrequencyScreenEvent
    data object OnDismissRequest : PickTaskFrequencyScreenEvent
}