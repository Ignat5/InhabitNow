package com.example.inhabitnow.android.presentation.create_edit_task.common.config.pick_tags.components

import com.example.inhabitnow.android.presentation.base.components.result.ScreenResult

sealed interface PickTaskTagsScreenResult : ScreenResult {
    data class Confirm(val tagIds: Set<String>) : PickTaskTagsScreenResult
    data object ManageTags : PickTaskTagsScreenResult
    data object Dismiss : PickTaskTagsScreenResult
}