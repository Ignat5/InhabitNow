package com.example.inhabitnow.android.presentation.create_edit_task.common.config.pick_tags.model

import androidx.compose.runtime.Stable
import com.example.inhabitnow.domain.model.tag.TagModel

@Stable
data class SelectableTagModel(
    val tagModel: TagModel,
    val isSelected: Boolean
)
