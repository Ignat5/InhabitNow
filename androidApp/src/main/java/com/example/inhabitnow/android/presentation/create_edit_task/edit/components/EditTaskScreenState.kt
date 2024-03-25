package com.example.inhabitnow.android.presentation.create_edit_task.edit.components

import com.example.inhabitnow.android.presentation.base.components.state.ScreenState
import com.example.inhabitnow.android.presentation.create_edit_task.common.config.model.BaseItemTaskConfig
import com.example.inhabitnow.android.presentation.create_edit_task.edit.model.ItemTaskAction

data class EditTaskScreenState(
    val allTaskConfigItems: List<BaseItemTaskConfig>,
    val allTaskActionItems: List<ItemTaskAction>
) : ScreenState
