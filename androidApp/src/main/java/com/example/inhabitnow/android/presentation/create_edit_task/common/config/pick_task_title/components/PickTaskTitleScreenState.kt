package com.example.inhabitnow.android.presentation.create_edit_task.common.config.pick_task_title.components

import androidx.compose.runtime.Immutable
import com.example.inhabitnow.android.presentation.base.components.state.ScreenState

@Immutable
data class PickTaskTitleScreenState(
    val inputTitle: String,
    val canConfirm: Boolean
) : ScreenState
