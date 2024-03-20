package com.example.inhabitnow.android.presentation.create_edit_task.common.config.pick_tags.model

import com.example.inhabitnow.domain.model.tag.TagModel

data class SelectableTagModel(
    val tagModel: TagModel,
    val isSelected: Boolean
)
