package com.example.inhabitnow.android.presentation.create_task.components

import androidx.compose.runtime.Immutable
import com.example.inhabitnow.android.presentation.base.components.state.ScreenState
import com.example.inhabitnow.domain.model.task.TaskWithContentModel

@Immutable
data class CreateTaskScreenState(
    val taskWithContent: TaskWithContentModel?,
    val canSave: Boolean
) : ScreenState
