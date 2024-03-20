package com.example.inhabitnow.android.presentation.view_tags.config.create_edit_tag.base.components

import com.example.inhabitnow.android.presentation.base.components.state.ScreenState

interface BaseCreateEditTagScreenState : ScreenState {
    val inputTitle: String
    val canConfirm: Boolean
}