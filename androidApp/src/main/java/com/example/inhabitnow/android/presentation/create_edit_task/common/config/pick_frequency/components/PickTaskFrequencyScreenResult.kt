package com.example.inhabitnow.android.presentation.create_edit_task.common.config.pick_frequency.components

import com.example.inhabitnow.android.presentation.base.components.result.ScreenResult
import com.example.inhabitnow.android.presentation.model.UITaskContent

sealed interface PickTaskFrequencyScreenResult: ScreenResult {
    data class Confirm(
        val uiFrequencyContent: UITaskContent.Frequency
    ) : PickTaskFrequencyScreenResult

    data object Dismiss :  PickTaskFrequencyScreenResult
}