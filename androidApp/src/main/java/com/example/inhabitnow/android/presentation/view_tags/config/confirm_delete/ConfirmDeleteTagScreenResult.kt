package com.example.inhabitnow.android.presentation.view_tags.config.confirm_delete

import com.example.inhabitnow.android.presentation.base.components.result.ScreenResult

sealed interface ConfirmDeleteTagScreenResult : ScreenResult {
    data class Confirm(val tagId: String) : ConfirmDeleteTagScreenResult
    data object Dismiss : ConfirmDeleteTagScreenResult
}