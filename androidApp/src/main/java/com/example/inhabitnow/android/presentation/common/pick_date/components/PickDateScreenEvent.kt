package com.example.inhabitnow.android.presentation.common.pick_date.components

import com.example.inhabitnow.android.presentation.base.components.event.ScreenEvent
import com.example.inhabitnow.android.presentation.common.pick_date.model.UIDateItem

sealed interface PickDateScreenEvent : ScreenEvent {
    data class OnDateItemClick(val dateItem: UIDateItem) : PickDateScreenEvent
    data object OnNextMonthClick : PickDateScreenEvent
    data object OnPrevMonthClick : PickDateScreenEvent
    data object OnConfirmClick : PickDateScreenEvent
    data object OnDismissRequest : PickDateScreenEvent
}