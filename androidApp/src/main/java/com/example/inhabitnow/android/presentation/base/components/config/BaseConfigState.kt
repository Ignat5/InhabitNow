package com.example.inhabitnow.android.presentation.base.components.config

sealed interface BaseConfigState<out SC : ScreenConfig> {
    data object Idle : BaseConfigState<Nothing>
    data class Config<out SC : ScreenConfig>(val config: SC) : BaseConfigState<SC>
}