package com.example.inhabitnow.android.presentation.create_task.components

import com.example.inhabitnow.android.presentation.base.components.state.ScreenState
import com.example.inhabitnow.domain.model.task.TaskWithContentModel

data class CreateTaskScreenState(
    val taskWithContentModel: TaskWithContentModel?
) : ScreenState
