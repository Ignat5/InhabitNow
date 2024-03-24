package com.example.inhabitnow.android.presentation.create_edit_task.base.components

import com.example.inhabitnow.android.presentation.base.components.event.ScreenEvent
import com.example.inhabitnow.android.presentation.base.components.result.ScreenResult
import com.example.inhabitnow.android.presentation.common.pick_date.components.PickDateScreenResult
import com.example.inhabitnow.android.presentation.create_edit_task.common.config.model.BaseItemTaskConfig
import com.example.inhabitnow.android.presentation.create_edit_task.common.config.pick_description.components.PickTaskDescriptionScreenResult
import com.example.inhabitnow.android.presentation.create_edit_task.common.config.pick_frequency.components.PickTaskFrequencyScreenResult
import com.example.inhabitnow.android.presentation.create_edit_task.common.config.pick_progress.number.components.PickTaskNumberProgressScreenResult
import com.example.inhabitnow.android.presentation.create_edit_task.common.config.pick_progress.time.components.PickTaskTimeProgressScreenResult
import com.example.inhabitnow.android.presentation.create_edit_task.common.config.pick_task_title.components.PickTaskTitleScreenResult

sealed interface BaseCreateEditTaskScreenEvent : ScreenEvent {
    data class OnBaseItemTaskConfigClick(
        val item: BaseItemTaskConfig
    ) : BaseCreateEditTaskScreenEvent

    sealed interface ResultEvent : BaseCreateEditTaskScreenEvent {
        val result: ScreenResult

        data class PickTaskTitle(
            override val result: PickTaskTitleScreenResult
        ) : ResultEvent

        data class PickTaskDescription(
            override val result: PickTaskDescriptionScreenResult
        ) : ResultEvent

        data class PickTaskNumberProgress(
            override val result: PickTaskNumberProgressScreenResult
        ) : ResultEvent

        data class PickTaskTimeProgress(
            override val result: PickTaskTimeProgressScreenResult
        ) : ResultEvent

        data class PickTaskFrequency(
            override val result: PickTaskFrequencyScreenResult
        ) : ResultEvent

        sealed interface PickTaskDate : ResultEvent {
            override val result: PickDateScreenResult

            data class StartDate(override val result: PickDateScreenResult) : PickTaskDate
            data class EndDate(override val result: PickDateScreenResult) : PickTaskDate
            data class OneDayDate(override val result: PickDateScreenResult) : PickTaskDate
        }
    }
}