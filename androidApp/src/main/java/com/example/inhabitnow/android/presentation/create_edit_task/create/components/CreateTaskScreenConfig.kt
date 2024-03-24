package com.example.inhabitnow.android.presentation.create_edit_task.create.components

import com.example.inhabitnow.android.presentation.base.components.config.ScreenConfig
import com.example.inhabitnow.android.presentation.common.pick_date.PickDateStateHolder
import com.example.inhabitnow.android.presentation.create_edit_task.base.components.BaseCreateEditTaskScreenConfig
import com.example.inhabitnow.android.presentation.create_edit_task.common.config.pick_description.PickTaskDescriptionStateHolder
import com.example.inhabitnow.android.presentation.create_edit_task.common.config.pick_frequency.PickTaskFrequencyStateHolder
import com.example.inhabitnow.android.presentation.create_edit_task.common.config.pick_priority.PickTaskPriorityStateHolder
import com.example.inhabitnow.android.presentation.create_edit_task.common.config.pick_task_title.PickTaskTitleStateHolder
import com.example.inhabitnow.android.presentation.create_edit_task.common.config.pick_progress.number.PickTaskNumberProgressStateHolder
import com.example.inhabitnow.android.presentation.create_edit_task.common.config.pick_progress.time.PickTaskTimeProgressStateHolder
import com.example.inhabitnow.android.presentation.create_edit_task.common.config.pick_tags.PickTaskTagsStateHolder

sealed interface CreateTaskScreenConfig : ScreenConfig {
    data class Base(val baseConfig: BaseCreateEditTaskScreenConfig) : CreateTaskScreenConfig
    data object ConfirmLeave : CreateTaskScreenConfig
}