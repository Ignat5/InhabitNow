package com.example.inhabitnow.android.presentation.create_edit_task.common.config.progress.number.components

import androidx.compose.runtime.Immutable
import com.example.inhabitnow.android.presentation.base.components.state.ScreenState
import com.example.inhabitnow.core.type.ProgressLimitType

@Immutable
data class PickTaskNumberProgressScreenState(
    val limitType: ProgressLimitType,
    val limitNumber: String,
    val limitUnit: String,
    val canSave: Boolean
) : ScreenState
