package com.example.inhabitnow.android.presentation.common.pick_date.components

import com.example.inhabitnow.android.presentation.base.components.result.ScreenResult
import kotlinx.datetime.LocalDate

sealed interface PickDateScreenResult : ScreenResult {
    data class Confirm(val date: LocalDate) : PickDateScreenResult
    data object Dismiss : PickDateScreenResult
}