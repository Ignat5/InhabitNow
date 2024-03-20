package com.example.inhabitnow.android.presentation.view_tags

import androidx.lifecycle.viewModelScope
import com.example.inhabitnow.android.presentation.base.view_model.BaseViewModel
import com.example.inhabitnow.android.presentation.model.UIResultModel
import com.example.inhabitnow.android.presentation.view_tags.components.ViewTagsScreenConfig
import com.example.inhabitnow.android.presentation.view_tags.components.ViewTagsScreenEvent
import com.example.inhabitnow.android.presentation.view_tags.components.ViewTagsScreenNavigation
import com.example.inhabitnow.android.presentation.view_tags.components.ViewTagsScreenState
import com.example.inhabitnow.android.presentation.view_tags.config.confirm_delete.ConfirmDeleteTagScreenResult
import com.example.inhabitnow.android.presentation.view_tags.config.create_edit_tag.create.CreateTagStateHolder
import com.example.inhabitnow.android.presentation.view_tags.config.create_edit_tag.create.components.CreateTagScreenResult
import com.example.inhabitnow.android.presentation.view_tags.config.create_edit_tag.edit.EditTagStateHolder
import com.example.inhabitnow.android.presentation.view_tags.config.create_edit_tag.edit.components.EditTagScreenResult
import com.example.inhabitnow.domain.use_case.tag.delete_tag.DeleteTagByIdUseCase
import com.example.inhabitnow.domain.use_case.tag.read_tags.ReadTagsUseCase
import com.example.inhabitnow.domain.use_case.tag.save_tag.SaveTagUseCase
import com.example.inhabitnow.domain.use_case.tag.update_tag_by_id.UpdateTagByIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ViewTagsViewModel @Inject constructor(
    private val readTagsUseCase: ReadTagsUseCase,
    private val saveTagUseCase: SaveTagUseCase,
    private val updateTagByIdUseCase: UpdateTagByIdUseCase,
    private val deleteTagByIdUseCase: DeleteTagByIdUseCase
) : BaseViewModel<ViewTagsScreenEvent, ViewTagsScreenState, ViewTagsScreenNavigation, ViewTagsScreenConfig>() {

    private val allTagsState = readTagsUseCase()
        .map { it.sortedBy { it.createdAt } }
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            emptyList()
        )

    override val uiScreenState: StateFlow<ViewTagsScreenState> = allTagsState.map { allTags ->
        ViewTagsScreenState(
            result = if (allTags.isEmpty()) UIResultModel.NoData
            else UIResultModel.Data(allTags)
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000L),
        ViewTagsScreenState(
            result = UIResultModel.Loading(allTagsState.value)
        )
    )

    override fun onEvent(event: ViewTagsScreenEvent) {
        when (event) {
            is ViewTagsScreenEvent.ResultEvent -> onResultEvent(event)
            is ViewTagsScreenEvent.OnTagClick -> onTagClick(event)
            is ViewTagsScreenEvent.OnDeleteTagClick -> onDeleteTagClick(event)
            is ViewTagsScreenEvent.OnCreateTagClick -> onCreateTagClick()
            is ViewTagsScreenEvent.OnBackRequest -> onBackRequest()
        }
    }

    private fun onResultEvent(event: ViewTagsScreenEvent.ResultEvent) {
        when (event) {
            is ViewTagsScreenEvent.ResultEvent.CreateTag ->
                onCreateTagResultEvent(event)

            is ViewTagsScreenEvent.ResultEvent.EditTag ->
                onEditTagResultEvent(event)

            is ViewTagsScreenEvent.ResultEvent.ConfirmDeleteTag ->
                onConfirmDeleteTagResultEvent(event)
        }
    }

    private fun onConfirmDeleteTagResultEvent(event: ViewTagsScreenEvent.ResultEvent.ConfirmDeleteTag) {
        onIdleToAction {
            when (val result = event.result) {
                is ConfirmDeleteTagScreenResult.Confirm -> onConfirmDeleteTag(result)
                is ConfirmDeleteTagScreenResult.Dismiss -> Unit
            }
        }
    }

    private fun onConfirmDeleteTag(result: ConfirmDeleteTagScreenResult.Confirm) {
        viewModelScope.launch {
            deleteTagByIdUseCase(result.tagId)
        }
    }

    private fun onEditTagResultEvent(event: ViewTagsScreenEvent.ResultEvent.EditTag) {
        onIdleToAction {
            when (val result = event.result) {
                is EditTagScreenResult.Confirm -> onConfirmEditTag(result)
                is EditTagScreenResult.Dismiss -> Unit
            }
        }
    }

    private fun onConfirmEditTag(result: EditTagScreenResult.Confirm) {
        viewModelScope.launch {
            updateTagByIdUseCase(
                tagId = result.tagId,
                tagTitle = result.tagTitle
            )
        }
    }

    private fun onCreateTagResultEvent(event: ViewTagsScreenEvent.ResultEvent.CreateTag) {
        onIdleToAction {
            when (val result = event.result) {
                is CreateTagScreenResult.Confirm -> onConfirmCreateTag(result)
                is CreateTagScreenResult.Dismiss -> Unit
            }
        }
    }

    private fun onConfirmCreateTag(result: CreateTagScreenResult.Confirm) {
        viewModelScope.launch {
            saveTagUseCase(tagTitle = result.tagTitle)
        }
    }

    private fun onTagClick(event: ViewTagsScreenEvent.OnTagClick) {
        allTagsState.value.find { it.id == event.tagId }?.let { tagModel ->
            setUpConfigState(
                ViewTagsScreenConfig.EditTag(
                    stateHolder = EditTagStateHolder(
                        tagId = tagModel.id,
                        tagTitle = tagModel.title,
                        holderScope = provideChildScope()
                    )
                )
            )
        }
    }

    private fun onDeleteTagClick(event: ViewTagsScreenEvent.OnDeleteTagClick) {
        setUpConfigState(ViewTagsScreenConfig.ConfirmDeleteTag(event.tagId))
    }

    private fun onCreateTagClick() {
        setUpConfigState(
            ViewTagsScreenConfig.CreateTag(
                stateHolder = CreateTagStateHolder(
                    holderScope = provideChildScope()
                )
            )
        )
    }

    private fun onBackRequest() {
        setUpNavigationState(ViewTagsScreenNavigation.Back)
    }


}