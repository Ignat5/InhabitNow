package com.example.inhabitnow.android.presentation.create_edit_task.create.components

import com.example.inhabitnow.android.presentation.base.components.event.ScreenEvent
import com.example.inhabitnow.android.presentation.base.components.result.ScreenResult
import com.example.inhabitnow.android.presentation.common.pick_date.components.PickDateScreenResult
import com.example.inhabitnow.android.presentation.create_edit_task.common.config.model.ItemTaskConfig
import com.example.inhabitnow.android.presentation.create_edit_task.common.config.pick_description.components.PickTaskDescriptionScreenResult
import com.example.inhabitnow.android.presentation.create_edit_task.common.config.pick_frequency.components.PickTaskFrequencyScreenResult
import com.example.inhabitnow.android.presentation.create_edit_task.common.config.pick_priority.components.PickTaskPriorityScreenResult
import com.example.inhabitnow.android.presentation.create_edit_task.common.config.pick_task_title.components.PickTaskTitleScreenResult
import com.example.inhabitnow.android.presentation.create_edit_task.common.config.pick_progress.number.components.PickTaskNumberProgressScreenResult
import com.example.inhabitnow.android.presentation.create_edit_task.common.config.pick_progress.time.components.PickTaskTimeProgressScreenResult
import com.example.inhabitnow.android.presentation.create_edit_task.common.config.pick_tags.components.PickTaskTagsScreenResult

sealed interface CreateTaskScreenEvent : ScreenEvent {
    data object OnSaveClick : CreateTaskScreenEvent
    data object OnDismissRequest : CreateTaskScreenEvent

    data class OnItemTaskConfigClick(val item: ItemTaskConfig) : CreateTaskScreenEvent
    data object OnEndDateSwitchClick : CreateTaskScreenEvent
    sealed interface ResultEvent : CreateTaskScreenEvent {
        val result: ScreenResult

        data class PickTaskTitle(
            override val result: PickTaskTitleScreenResult
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

        data class PickTaskTags(
            override val result: PickTaskTagsScreenResult
        ) : ResultEvent

        data class PickTaskDescription(
            override val result: PickTaskDescriptionScreenResult
        ) : ResultEvent

        data class PickTaskPriority(
            override val result: PickTaskPriorityScreenResult
        ) : ResultEvent

        sealed interface PickDate : ResultEvent {
            override val result: PickDateScreenResult

            data class StartDate(
                override val result: PickDateScreenResult
            ) : PickDate

            data class EndDate(
                override val result: PickDateScreenResult
            ) : PickDate

            data class OneDayDate(
                override val result: PickDateScreenResult
            ) : PickDate
        }
    }
}