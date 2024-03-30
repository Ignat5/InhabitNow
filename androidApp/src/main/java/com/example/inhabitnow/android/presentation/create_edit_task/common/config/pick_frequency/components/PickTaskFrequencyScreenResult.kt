package com.example.inhabitnow.android.presentation.create_edit_task.common.config.pick_frequency.components

import com.example.inhabitnow.android.presentation.base.components.result.ScreenResult
import com.example.inhabitnow.domain.model.task.content.TaskContentModel

sealed interface PickTaskFrequencyScreenResult: ScreenResult {
    data class Confirm(
        val frequencyContent: TaskContentModel.FrequencyContent
    ) : PickTaskFrequencyScreenResult

    data object Dismiss :  PickTaskFrequencyScreenResult
}