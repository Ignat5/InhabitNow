package com.example.inhabitnow.android.presentation.view_tags.components

import com.example.inhabitnow.android.presentation.base.components.config.ScreenConfig
import com.example.inhabitnow.android.presentation.view_tags.config.create_edit_tag.create.CreateTagStateHolder

sealed interface ViewTagsScreenConfig : ScreenConfig {
    data class CreateTag(val stateHolder: CreateTagStateHolder) : ViewTagsScreenConfig
}