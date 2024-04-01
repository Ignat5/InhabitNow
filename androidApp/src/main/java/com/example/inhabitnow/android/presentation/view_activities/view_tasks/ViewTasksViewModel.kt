package com.example.inhabitnow.android.presentation.view_activities.view_tasks

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.inhabitnow.android.core.di.qualifier.DefaultDispatcherQualifier
import com.example.inhabitnow.android.presentation.base.view_model.BaseViewModel
import com.example.inhabitnow.android.presentation.view_activities.model.TaskFilterByStatus
import com.example.inhabitnow.android.presentation.view_activities.model.TaskSort
import com.example.inhabitnow.android.presentation.view_activities.view_tasks.components.ViewTasksScreenConfig
import com.example.inhabitnow.android.presentation.view_activities.view_tasks.components.ViewTasksScreenEvent
import com.example.inhabitnow.android.presentation.view_activities.view_tasks.components.ViewTasksScreenNavigation
import com.example.inhabitnow.android.presentation.view_activities.view_tasks.components.ViewTasksScreenState
import com.example.inhabitnow.domain.model.task.derived.FullTaskModel
import com.example.inhabitnow.domain.use_case.read_full_tasks.ReadFullTasksUseCase
import com.example.inhabitnow.domain.use_case.tag.read_tags.ReadTagsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class ViewTasksViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    readFullTasksUseCase: ReadFullTasksUseCase,
    readTagsUseCase: ReadTagsUseCase,
    @DefaultDispatcherQualifier private val defaultDispatcher: CoroutineDispatcher
) : BaseViewModel<ViewTasksScreenEvent, ViewTasksScreenState, ViewTasksScreenNavigation, ViewTasksScreenConfig>() {
    private val filterByTagsIdsState = MutableStateFlow<Set<String>>(emptySet())
    private val filterByStatusState = MutableStateFlow<TaskFilterByStatus.HabitStatus?>(null)
    private val sortState = MutableStateFlow<TaskSort.HabitsSort?>(null)
    private val allTasksState = readFullTasksUseCase()
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            emptyList()
        )

    private val allProcessedTasksState = combine(
        allTasksState,
        filterByTagsIdsState,
        filterByStatusState,
        sortState
    ) { allTasks, filterByTagsIds, filterByStatus, sort ->
        allTasks
//            .asSequence()
//            .let { if (filterByTagsIds.isNotEmpty()) }
//            .toList()
    }

    override val uiScreenState: StateFlow<ViewTasksScreenState>
        get() = TODO("Not yet implemented")

    override fun onEvent(event: ViewTasksScreenEvent) {
        TODO("Not yet implemented")
    }

    private fun Sequence<FullTaskModel.FullTask>.filterByTags(
        filterByTagsIds: Set<String>
    ) = this.let { allTasks ->

    }

}