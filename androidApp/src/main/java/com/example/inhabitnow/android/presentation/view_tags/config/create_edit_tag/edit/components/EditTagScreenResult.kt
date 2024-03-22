package com.example.inhabitnow.android.presentation.view_tags.config.create_edit_tag.edit.components

import com.example.inhabitnow.android.presentation.base.components.result.ScreenResult

sealed interface EditTagScreenResult : ScreenResult {
    data class Confirm(
        val tagId: String,
        val tagTitle: String
    ) : EditTagScreenResult

    data object Dismiss : EditTagScreenResult
}