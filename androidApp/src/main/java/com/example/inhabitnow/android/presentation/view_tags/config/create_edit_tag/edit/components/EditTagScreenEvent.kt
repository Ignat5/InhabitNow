package com.example.inhabitnow.android.presentation.view_tags.config.create_edit_tag.edit.components

import com.example.inhabitnow.android.presentation.base.components.event.ScreenEvent
import com.example.inhabitnow.android.presentation.view_tags.config.create_edit_tag.base.components.BaseCreateEditTagScreenEvent

sealed interface EditTagScreenEvent : ScreenEvent {
    data class BaseEvent(val baseEvent: BaseCreateEditTagScreenEvent) : EditTagScreenEvent
}