package com.example.inhabitnow.android.presentation.create_edit_task.base.components

import com.example.inhabitnow.android.presentation.base.components.config.ScreenConfig
import com.example.inhabitnow.android.presentation.common.pick_date.PickDateStateHolder
import com.example.inhabitnow.android.presentation.create_edit_task.common.config.pick_description.PickTaskDescriptionStateHolder
import com.example.inhabitnow.android.presentation.create_edit_task.common.config.pick_frequency.PickTaskFrequencyStateHolder
import com.example.inhabitnow.android.presentation.create_edit_task.common.config.pick_progress.number.PickTaskNumberProgressStateHolder
import com.example.inhabitnow.android.presentation.create_edit_task.common.config.pick_progress.time.PickTaskTimeProgressStateHolder
import com.example.inhabitnow.android.presentation.create_edit_task.common.config.pick_task_title.PickTaskTitleStateHolder

sealed interface BaseCreateEditTaskScreenConfig : ScreenConfig {
    data class PickTaskTitle(
        val stateHolder: PickTaskTitleStateHolder
    ) : BaseCreateEditTaskScreenConfig

    data class PickTaskDescription(
        val stateHolder: PickTaskDescriptionStateHolder
    ) : BaseCreateEditTaskScreenConfig

    data class PickTaskNumberProgress(
        val stateHolder: PickTaskNumberProgressStateHolder
    ) : BaseCreateEditTaskScreenConfig

    data class PickTaskTimeProgress(
        val stateHolder: PickTaskTimeProgressStateHolder
    ) : BaseCreateEditTaskScreenConfig

    data class PickTaskFrequency(
        val stateHolder: PickTaskFrequencyStateHolder
    ) : BaseCreateEditTaskScreenConfig

    sealed interface PickDate : BaseCreateEditTaskScreenConfig {
        val stateHolder: PickDateStateHolder

        data class StartDate(
            override val stateHolder: PickDateStateHolder
        ) : PickDate

        data class EndDate(
            override val stateHolder: PickDateStateHolder
        ) : PickDate

        data class OneDayDate(
            override val stateHolder: PickDateStateHolder
        ) : PickDate
    }
}