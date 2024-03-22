package com.example.inhabitnow.android.presentation.view_tags.components

import com.example.inhabitnow.android.presentation.base.components.config.ScreenConfig
import com.example.inhabitnow.android.presentation.view_tags.config.create_edit_tag.create.CreateTagStateHolder
import com.example.inhabitnow.android.presentation.view_tags.config.create_edit_tag.edit.EditTagStateHolder

sealed interface ViewTagsScreenConfig : ScreenConfig {
    data class CreateTag(val stateHolder: CreateTagStateHolder) : ViewTagsScreenConfig
    data class EditTag(val stateHolder: EditTagStateHolder) : ViewTagsScreenConfig
    data class ConfirmDeleteTag(val tagId: String) : ViewTagsScreenConfig
}