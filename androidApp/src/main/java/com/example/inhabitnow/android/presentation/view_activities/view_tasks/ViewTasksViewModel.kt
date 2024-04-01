package com.example.inhabitnow.android.presentation.view_activities.view_tasks

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.inhabitnow.android.core.di.qualifier.DefaultDispatcherQualifier
import com.example.inhabitnow.android.presentation.base.view_model.BaseViewModel
import com.example.inhabitnow.android.presentation.view_activities.base.BaseViewTasksViewModel
import com.example.inhabitnow.android.presentation.view_activities.base.components.BaseViewTasksScreenConfig
import com.example.inhabitnow.android.presentation.view_activities.base.components.BaseViewTasksScreenNavigation
import com.example.inhabitnow.android.presentation.view_activities.model.TaskFilterByStatus
import com.example.inhabitnow.android.presentation.view_activities.model.TaskSort
import com.example.inhabitnow.android.presentation.view_activities.view_tasks.components.ViewTasksScreenConfig
import com.example.inhabitnow.android.presentation.view_activities.view_tasks.components.ViewTasksScreenEvent
import com.example.inhabitnow.android.presentation.view_activities.view_tasks.components.ViewTasksScreenNavigation
import com.example.inhabitnow.android.presentation.view_activities.view_tasks.components.ViewTasksScreenState
import com.example.inhabitnow.domain.model.task.derived.FullTaskModel
import com.example.inhabitnow.domain.use_case.archive_task_by_id.ArchiveTaskByIdUseCase
import com.example.inhabitnow.domain.use_case.delete_task_by_id.DeleteTaskByIdUseCase
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
    archiveTaskByIdUseCase: ArchiveTaskByIdUseCase,
    deleteTaskByIdUseCase: DeleteTaskByIdUseCase,
    @DefaultDispatcherQualifier private val defaultDispatcher: CoroutineDispatcher
) : BaseViewTasksViewModel<ViewTasksScreenEvent, ViewTasksScreenState, ViewTasksScreenNavigation, ViewTasksScreenConfig, TaskFilterByStatus.TaskStatus, TaskSort.TasksSort>(
    readTagsUseCase = readTagsUseCase,
    archiveTaskByIdUseCase = archiveTaskByIdUseCase,
    deleteTaskByIdUseCase = deleteTaskByIdUseCase,
    defaultDispatcher = defaultDispatcher
) {

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
            .asSequence()
            .let { if (filterByTagsIds.isNotEmpty()) it.filterByTags(filterByTagsIds) else it }
            .let { if (filterByStatus != null) it.filterByStatus(filterByStatus) else it }
            .sortHabits(sort)
            .toList()
    }

    override val uiScreenState: StateFlow<ViewTasksScreenState>
        get() = TODO("Not yet implemented")

    override fun onEvent(event: ViewTasksScreenEvent) {
        TODO("Not yet implemented")
    }

    override fun setUpBaseNavigationState(baseSN: BaseViewTasksScreenNavigation) {
        TODO("Not yet implemented")
    }

    override fun setUpBaseConfigState(baseConfig: BaseViewTasksScreenConfig) {
        TODO("Not yet implemented")
    }

    private fun Sequence<FullTaskModel.FullTask>.filterByStatus(
        filterByStatus: TaskFilterByStatus.TaskStatus
    ) = this.let { allTasks ->
        when (filterByStatus) {
            is TaskFilterByStatus.OnlyActive -> allTasks.filterByOnlyActive()
            is TaskFilterByStatus.OnlyArchived -> allTasks.filterByOnlyArchived()
        }
    }

    private fun Sequence<FullTaskModel.FullTask>.sortHabits(
        sort: TaskSort.TasksSort?
    ): Sequence<FullTaskModel.FullTask> = this.let { allHabits ->
        when (sort) {
            null -> allHabits.sortByDefault()
            is TaskSort.ByStartDate -> allHabits.sortByStartDate()
            is TaskSort.ByPriority -> allHabits.sortByPriority()
            is TaskSort.ByTitle -> allHabits.sortByTitle()
        }
    }

}