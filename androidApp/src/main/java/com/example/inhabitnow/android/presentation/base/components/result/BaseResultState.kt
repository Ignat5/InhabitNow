package com.example.inhabitnow.android.presentation.base.components.result

sealed interface BaseResultState<out SR : ScreenResult> {
    data object Idle : BaseResultState<Nothing>
    data class Result<out SR : ScreenResult>(val result: SR) : BaseResultState<SR>
}