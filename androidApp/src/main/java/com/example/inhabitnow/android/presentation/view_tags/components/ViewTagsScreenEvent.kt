package com.example.inhabitnow.android.presentation.view_tags.components

import com.example.inhabitnow.android.presentation.base.components.event.ScreenEvent
import com.example.inhabitnow.android.presentation.base.components.result.ScreenResult
import com.example.inhabitnow.android.presentation.view_tags.config.create_edit_tag.create.components.CreateTagScreenResult

sealed interface ViewTagsScreenEvent : ScreenEvent {
    data class OnTagClick(val tagId: String) : ViewTagsScreenEvent
    data class OnDeleteTagClick(val tagId: String) : ViewTagsScreenEvent
    data object OnCreateTagClick : ViewTagsScreenEvent
    data object OnBackRequest : ViewTagsScreenEvent

    sealed interface ResultEvent : ViewTagsScreenEvent {
        val result: ScreenResult

        data class CreateTag(
            override val result: CreateTagScreenResult
        ) : ResultEvent
    }
}