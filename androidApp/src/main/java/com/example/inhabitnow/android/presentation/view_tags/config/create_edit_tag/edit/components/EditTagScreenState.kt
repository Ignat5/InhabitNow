package com.example.inhabitnow.android.presentation.view_tags.config.create_edit_tag.edit.components

import com.example.inhabitnow.android.presentation.view_tags.config.create_edit_tag.base.components.BaseCreateEditTagScreenState

data class EditTagScreenState(
    override val inputTitle: String,
    override val canConfirm: Boolean
) : BaseCreateEditTagScreenState
