package com.example.inhabitnow.android.presentation.create_edit_task.common.config.pick_tags.components

import com.example.inhabitnow.android.presentation.base.components.state.ScreenState
import com.example.inhabitnow.android.presentation.create_edit_task.common.config.pick_tags.model.SelectableTagModel
import com.example.inhabitnow.domain.model.tag.TagModel

data class PickTaskTagsScreenState(
    val allSelectableTags: List<SelectableTagModel>
) : ScreenState
