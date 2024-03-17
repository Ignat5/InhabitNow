package com.example.inhabitnow.android.presentation.create_edit_task.create.components

import com.example.inhabitnow.android.presentation.base.components.config.ScreenConfig
import com.example.inhabitnow.android.presentation.create_edit_task.common.config.pick_frequency.PickTaskFrequencyStateHolder
import com.example.inhabitnow.android.presentation.create_edit_task.common.config.pick_task_title.PickTaskTitleStateHolder
import com.example.inhabitnow.android.presentation.create_edit_task.common.config.pick_progress.number.PickTaskNumberProgressStateHolder
import com.example.inhabitnow.android.presentation.create_edit_task.common.config.pick_progress.time.PickTaskTimeProgressStateHolder

sealed interface CreateTaskScreenConfig : ScreenConfig {
    data class PickTitle(
        val stateHolder: PickTaskTitleStateHolder
    ) : CreateTaskScreenConfig

    data class PickTaskNumberProgress(
        val stateHolder: PickTaskNumberProgressStateHolder
    ) : CreateTaskScreenConfig

    data class PickTaskTimeProgress(
        val stateHolder: PickTaskTimeProgressStateHolder
    ) : CreateTaskScreenConfig

    data class PickTaskFrequency(
        val stateHolder: PickTaskFrequencyStateHolder
    ) : CreateTaskScreenConfig
}