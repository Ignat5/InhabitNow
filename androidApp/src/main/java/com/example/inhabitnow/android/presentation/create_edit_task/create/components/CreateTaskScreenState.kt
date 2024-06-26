package com.example.inhabitnow.android.presentation.create_edit_task.create.components

import androidx.compose.runtime.Immutable
import com.example.inhabitnow.android.presentation.base.components.state.ScreenState
import com.example.inhabitnow.android.presentation.create_edit_task.common.config.model.BaseItemTaskConfig

@Immutable
data class CreateTaskScreenState(
    val allTaskConfigItems: List<BaseItemTaskConfig>,
    val canSave: Boolean
) : ScreenState
