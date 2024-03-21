package com.example.inhabitnow.android.presentation.view_tags.config.create_edit_tag.create.components

import com.example.inhabitnow.android.presentation.base.components.state.ScreenState
import com.example.inhabitnow.android.presentation.view_tags.config.create_edit_tag.base.components.BaseCreateEditTagScreenState

data class CreateTagScreenState(
    override val inputTitle: String,
    override val canConfirm: Boolean
) : BaseCreateEditTagScreenState
