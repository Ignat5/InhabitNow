package com.example.inhabitnow.android.presentation.view_tags

import androidx.lifecycle.viewModelScope
import com.example.inhabitnow.android.presentation.base.view_model.BaseViewModel
import com.example.inhabitnow.android.presentation.model.UIResultModel
import com.example.inhabitnow.android.presentation.view_tags.components.ViewTagsScreenConfig
import com.example.inhabitnow.android.presentation.view_tags.components.ViewTagsScreenEvent
import com.example.inhabitnow.android.presentation.view_tags.components.ViewTagsScreenNavigation
import com.example.inhabitnow.android.presentation.view_tags.components.ViewTagsScreenState
import com.example.inhabitnow.android.presentation.view_tags.config.create_edit_tag.create.CreateTagStateHolder
import com.example.inhabitnow.domain.use_case.tag.delete_tag.DeleteTagByIdUseCase
import com.example.inhabitnow.domain.use_case.tag.read_tags.ReadTagsUseCase
import com.example.inhabitnow.domain.use_case.tag.save_tag.SaveTagUseCase
import com.example.inhabitnow.domain.use_case.tag.update_tag_by_id.UpdateTagByIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
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

    }

    private fun onTagClick(event: ViewTagsScreenEvent.OnTagClick) {

    }

    private fun onDeleteTagClick(event: ViewTagsScreenEvent.OnDeleteTagClick) {

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