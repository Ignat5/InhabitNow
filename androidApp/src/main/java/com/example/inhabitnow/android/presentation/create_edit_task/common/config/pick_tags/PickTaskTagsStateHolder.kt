package com.example.inhabitnow.android.presentation.create_edit_task.common.config.pick_tags

import com.example.inhabitnow.android.presentation.base.state_holder.BaseResultStateHolder
import com.example.inhabitnow.android.presentation.create_edit_task.common.config.pick_tags.components.PickTaskTagsScreenEvent
import com.example.inhabitnow.android.presentation.create_edit_task.common.config.pick_tags.components.PickTaskTagsScreenResult
import com.example.inhabitnow.android.presentation.create_edit_task.common.config.pick_tags.components.PickTaskTagsScreenState
import com.example.inhabitnow.android.presentation.create_edit_task.common.config.pick_tags.model.SelectableTagModel
import com.example.inhabitnow.domain.model.tag.TagModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class PickTaskTagsStateHolder(
    private val allTags: List<TagModel>,
    initSelectedTagIds: Set<String>,
    override val holderScope: CoroutineScope
) : BaseResultStateHolder<PickTaskTagsScreenEvent, PickTaskTagsScreenState, PickTaskTagsScreenResult>() {

    private val selectedIdsState = MutableStateFlow<Set<String>>(initSelectedTagIds)

    private val allSelectableTagsState = selectedIdsState.map { selectedIds ->
        allTags.map { tagModel ->
            SelectableTagModel(
                tagModel = tagModel,
                isSelected = tagModel.id in selectedIds
            )
        }
    }.stateIn(
        holderScope,
        SharingStarted.Eagerly,
        emptyList()
    )

    override val uiScreenState: StateFlow<PickTaskTagsScreenState> =
        allSelectableTagsState.map { allSelectableTags ->
            PickTaskTagsScreenState(
                allSelectableTags = allSelectableTags
            )
        }.stateIn(
            holderScope,
            SharingStarted.WhileSubscribed(5000L),
            PickTaskTagsScreenState(
                allSelectableTags = allSelectableTagsState.value
            )
        )

    override fun onEvent(event: PickTaskTagsScreenEvent) {
        when (event) {
            is PickTaskTagsScreenEvent.OnTagClick -> onTagClick(event)
            is PickTaskTagsScreenEvent.OnManageTagsClick -> onManageTagsClick()
            is PickTaskTagsScreenEvent.OnConfirmClick -> onConfirmClick()
            is PickTaskTagsScreenEvent.OnDismissRequest -> onDismissRequest()
        }
    }

    private fun onTagClick(event: PickTaskTagsScreenEvent.OnTagClick) {
        val clickedTagId = event.tagId
        selectedIdsState.update { oldSet ->
            val newSet = mutableSetOf<String>()
            newSet.addAll(oldSet)
            if (newSet.contains(clickedTagId)) newSet.remove(clickedTagId)
            else newSet.add(clickedTagId)
            newSet
        }
    }

    private fun onManageTagsClick() {
        setUpResult(PickTaskTagsScreenResult.ManageTags)
    }

    private fun onConfirmClick() {
        setUpResult(PickTaskTagsScreenResult.Confirm(selectedIdsState.value))
    }

    private fun onDismissRequest() {
        setUpResult(PickTaskTagsScreenResult.Dismiss)
    }

}