package com.example.inhabitnow.android.presentation.create_edit_task.common.config.pick_frequency.components

import androidx.compose.runtime.Immutable
import com.example.inhabitnow.android.presentation.base.components.state.ScreenState
import com.example.inhabitnow.android.presentation.model.UITaskContent

@Immutable
data class PickTaskFrequencyScreenState(
    val uiFrequencyContent: UITaskContent.Frequency,
    val canConfirm: Boolean
) : ScreenState
