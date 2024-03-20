package com.example.inhabitnow.android.presentation.create_edit_task.common.config.pick_tags.components

import com.example.inhabitnow.android.presentation.base.components.event.ScreenEvent

sealed interface PickTaskTagsScreenEvent : ScreenEvent {
    data class OnTagClick(val tagId: String) : PickTaskTagsScreenEvent
    data object OnManageTagsClick : PickTaskTagsScreenEvent
    data object OnConfirmClick : PickTaskTagsScreenEvent
    data object OnDismissRequest : PickTaskTagsScreenEvent

}