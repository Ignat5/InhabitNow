package com.example.inhabitnow.android.presentation.common.pick_date.components

import androidx.compose.runtime.Immutable
import com.example.inhabitnow.android.presentation.base.components.state.ScreenState
import com.example.inhabitnow.android.presentation.common.pick_date.model.UIDateItem
import kotlinx.datetime.LocalDate

@Immutable
data class PickDateScreenState(
    val currentDate: LocalDate,
    val allDaysOfMonth: List<UIDateItem>,
    val currentPickedDate: LocalDate,
    val todayDate: LocalDate,
) : ScreenState
