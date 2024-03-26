package com.example.inhabitnow.android.presentation.search_tasks

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.inhabitnow.android.presentation.base.view_model.BaseViewModel
import com.example.inhabitnow.android.presentation.search_tasks.components.SearchTasksScreenConfig
import com.example.inhabitnow.android.presentation.search_tasks.components.SearchTasksScreenEvent
import com.example.inhabitnow.android.presentation.search_tasks.components.SearchTasksScreenNavigation
import com.example.inhabitnow.android.presentation.search_tasks.components.SearchTasksScreenState
import com.example.inhabitnow.domain.model.task.TaskModel
import com.example.inhabitnow.domain.use_case.read_tasks_by_search_query.ReadTasksBySearchQueryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class SearchTasksViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val readTasksBySearchQueryUseCase: ReadTasksBySearchQueryUseCase
) : BaseViewModel<SearchTasksScreenEvent, SearchTasksScreenState, SearchTasksScreenNavigation, SearchTasksScreenConfig>() {

    private val searchQueryState = savedStateHandle.getStateFlow(
        key = SEARCH_QUERY_KEY,
        initialValue = DEFAULT_SEARCH_QUERY
    )

    @OptIn(ExperimentalCoroutinesApi::class)
    private val allTasksWithContentState: StateFlow<List<TaskModel>> =
        searchQueryState.flatMapLatest { searchQuery ->
            if (searchQuery.isNotBlank()) readTasksBySearchQueryUseCase(searchQuery)
            else flow { emit(emptyList()) }
        }.stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            emptyList()
        )

    override val uiScreenState: StateFlow<SearchTasksScreenState> =
        combine(searchQueryState, allTasksWithContentState) { searchQuery, allTasksWithContent ->
            SearchTasksScreenState(
                searchQuery = searchQuery,
                allTasksWithContent = allTasksWithContent,
                canClearSearch = searchQuery.isNotEmpty()
            )
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            SearchTasksScreenState(
                searchQuery = searchQueryState.value,
                allTasksWithContent = allTasksWithContentState.value,
                canClearSearch = searchQueryState.value.isNotEmpty()
            )
        )

    override fun onEvent(event: SearchTasksScreenEvent) {
        when (event) {
            is SearchTasksScreenEvent.OnInputUpdateSearchQuery -> onInputUpdateSearchQuery(event)
            is SearchTasksScreenEvent.OnTaskClick -> onTaskClick(event)
            is SearchTasksScreenEvent.OnClearSearchClick -> onClearSearchClick()
            is SearchTasksScreenEvent.OnBackRequest -> onBackRequest()
        }
    }

    private fun onInputUpdateSearchQuery(event: SearchTasksScreenEvent.OnInputUpdateSearchQuery) {
        savedStateHandle[SEARCH_QUERY_KEY] = event.value
    }

    private fun onTaskClick(event: SearchTasksScreenEvent.OnTaskClick) {
        setUpNavigationState(SearchTasksScreenNavigation.EditTask(event.taskId))
    }

    private fun onClearSearchClick() {
        savedStateHandle[SEARCH_QUERY_KEY] = DEFAULT_SEARCH_QUERY
    }

    private fun onBackRequest() {
        setUpNavigationState(SearchTasksScreenNavigation.Back)
    }

    companion object {
        private const val DEFAULT_SEARCH_QUERY = ""
        private const val SEARCH_QUERY_KEY = "SEARCH_QUERY_KEY"
    }

}