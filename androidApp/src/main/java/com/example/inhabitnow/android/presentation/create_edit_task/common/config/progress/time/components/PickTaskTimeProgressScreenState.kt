package com.example.inhabitnow.android.presentation.create_edit_task.common.config.progress.time.components

import androidx.compose.runtime.Immutable
import com.example.inhabitnow.android.presentation.base.components.state.ScreenState
import com.example.inhabitnow.core.type.ProgressLimitType

@Immutable
data class PickTaskTimeProgressScreenState(
    val limitType: ProgressLimitType,
    val limitHours: Int,
    val limitMinutes: Int,
) : ScreenState
