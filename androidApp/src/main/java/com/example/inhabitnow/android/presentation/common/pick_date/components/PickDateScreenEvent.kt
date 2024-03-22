package com.example.inhabitnow.android.presentation.common.pick_date.components

import com.example.inhabitnow.android.presentation.base.components.event.ScreenEvent
import com.example.inhabitnow.android.presentation.common.pick_date.model.UIDateItem
import kotlinx.datetime.LocalDate

sealed interface PickDateScreenEvent : ScreenEvent {
    data class OnDateItemClick(val date: LocalDate) : PickDateScreenEvent
    data object OnNextMonthClick : PickDateScreenEvent
    data object OnPrevMonthClick : PickDateScreenEvent
    data object OnConfirmClick : PickDateScreenEvent
    data object OnDismissRequest : PickDateScreenEvent
}