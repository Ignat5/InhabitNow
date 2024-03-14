package com.example.inhabitnow.android.presentation.create_edit_task.common.config.progress.number.components

import com.example.inhabitnow.android.presentation.base.components.result.ScreenResult
import com.example.inhabitnow.domain.model.task.content.TaskContentModel

sealed interface PickTaskNumberProgressScreenResult : ScreenResult {
    data class Confirm(
        val progressContent: TaskContentModel.ProgressContent.Number
    ) : PickTaskNumberProgressScreenResult

    data object Dismiss : PickTaskNumberProgressScreenResult
}