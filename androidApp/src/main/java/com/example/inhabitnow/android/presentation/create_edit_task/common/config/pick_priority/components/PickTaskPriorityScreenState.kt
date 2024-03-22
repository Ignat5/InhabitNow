package com.example.inhabitnow.android.presentation.create_edit_task.common.config.pick_priority.components

import com.example.inhabitnow.android.presentation.base.components.state.ScreenState

data class PickTaskPriorityScreenState(
    val inputPriority: String,
    val canConfirm: Boolean,
    val valueValidator: (String) -> Boolean
) : ScreenState
