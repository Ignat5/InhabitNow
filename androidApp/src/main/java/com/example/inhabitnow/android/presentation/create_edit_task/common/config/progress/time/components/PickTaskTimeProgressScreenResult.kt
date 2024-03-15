package com.example.inhabitnow.android.presentation.create_edit_task.common.config.progress.time.components

import com.example.inhabitnow.android.presentation.base.components.result.ScreenResult
import com.example.inhabitnow.domain.model.task.content.TaskContentModel

sealed interface PickTaskTimeProgressScreenResult : ScreenResult {
    data class Confirm(
        val progressContent: TaskContentModel.ProgressContent.Time
    ) : PickTaskTimeProgressScreenResult

    data object Dismiss : PickTaskTimeProgressScreenResult
}