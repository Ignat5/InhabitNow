package com.example.inhabitnow.android.presentation.create_edit_task.common.config.pick_tags.components

import androidx.compose.runtime.Immutable
import com.example.inhabitnow.android.presentation.base.components.state.ScreenState
import com.example.inhabitnow.android.presentation.create_edit_task.common.config.pick_tags.model.SelectableTagModel
import com.example.inhabitnow.android.presentation.model.UIResultModel
import com.example.inhabitnow.domain.model.tag.TagModel

@Immutable
data class PickTaskTagsScreenState(
    val allSelectableTagsResultModel: UIResultModel<List<SelectableTagModel>>
) : ScreenState
