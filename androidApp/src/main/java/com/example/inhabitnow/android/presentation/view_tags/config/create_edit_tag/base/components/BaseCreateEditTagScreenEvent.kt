package com.example.inhabitnow.android.presentation.view_tags.config.create_edit_tag.base.components

import com.example.inhabitnow.android.presentation.base.components.event.ScreenEvent

sealed interface BaseCreateEditTagScreenEvent : ScreenEvent {
    data class OnInputUpdateTitle(val title: String) : BaseCreateEditTagScreenEvent
    data object OnConfirmClick : BaseCreateEditTagScreenEvent
    data object OnDismissRequest : BaseCreateEditTagScreenEvent
}