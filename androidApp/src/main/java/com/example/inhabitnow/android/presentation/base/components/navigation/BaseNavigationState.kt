package com.example.inhabitnow.android.presentation.base.components.navigation

sealed interface BaseNavigationState<out SN: ScreenNavigation> {
    data object Idle : BaseNavigationState<Nothing>
    data class Destination<out SN: ScreenNavigation>(val destination: SN) : BaseNavigationState<SN>
}