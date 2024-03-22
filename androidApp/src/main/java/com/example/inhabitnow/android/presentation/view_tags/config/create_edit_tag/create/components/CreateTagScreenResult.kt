package com.example.inhabitnow.android.presentation.view_tags.config.create_edit_tag.create.components

import com.example.inhabitnow.android.presentation.base.components.result.ScreenResult

sealed interface CreateTagScreenResult : ScreenResult {
    data class Confirm(val tagTitle: String) : CreateTagScreenResult
    data object Dismiss : CreateTagScreenResult
}