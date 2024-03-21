package com.example.inhabitnow.android.presentation.view_tags.components

import androidx.compose.runtime.Immutable
import com.example.inhabitnow.android.presentation.base.components.state.ScreenState
import com.example.inhabitnow.android.presentation.model.UIResultModel
import com.example.inhabitnow.domain.model.tag.TagModel

@Immutable
data class ViewTagsScreenState(
    val result: UIResultModel<List<TagModel>>
) : ScreenState
