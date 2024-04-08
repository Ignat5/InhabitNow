package com.example.inhabitnow.android.presentation.view_activities.view_tasks

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.inhabitnow.android.core.di.qualifier.DefaultDispatcherQualifier
import com.example.inhabitnow.android.presentation.base.view_model.BaseViewModel
import com.example.inhabitnow.android.presentation.main.config.pick_task_type.PickTaskTypeScreenResult
import com.example.inhabitnow.android.presentation.model.UIResultModel
import com.example.inhabitnow.android.presentation.view_activities.base.BaseViewTasksViewModel
import com.example.inhabitnow.android.presentation.view_activities.base.components.BaseViewTasksScreenConfig
import com.example.inhabitnow.android.presentation.view_activities.base.components.BaseViewTasksScreenNavigation
import com.example.inhabitnow.android.presentation.view_activities.model.TaskFilterByStatus
import com.example.inhabitnow.android.presentation.view_activities.model.TaskSort
import com.example.inhabitnow.android.presentation.view_activities.view_tasks.components.ViewTasksScreenConfig
import com.example.inhabitnow.android.presentation.view_activities.view_tasks.components.ViewTasksScreenEvent
import com.example.inhabitnow.android.presentation.view_activities.view_tasks.components.ViewTasksScreenNavigation
import com.example.inhabitnow.android.presentation.view_activities.view_tasks.components.ViewTasksScreenState
import com.example.inhabitnow.core.model.ResultModel
import com.example.inhabitnow.core.type.TaskType
import com.example.inhabitnow.domain.model.task.derived.FullTaskModel
import com.example.inhabitnow.domain.use_case.archive_task_by_id.ArchiveTaskByIdUseCase
import com.example.inhabitnow.domain.use_case.delete_task_by_id.DeleteTaskByIdUseCase
import com.example.inhabitnow.domain.use_case.read_full_tasks.ReadFullTasksUseCase
import com.example.inhabitnow.domain.use_case.save_default_task.SaveDefaultTaskUseCase
import com.example.inhabitnow.domain.use_case.tag.read_tags.ReadTagsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ViewTasksViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    readFullTasksUseCase: ReadFullTasksUseCase,
    readTagsUseCase: ReadTagsUseCase,
    archiveTaskByIdUseCase: ArchiveTaskByIdUseCase,
    deleteTaskByIdUseCase: DeleteTaskByIdUseCase,
    private val saveDefaultTaskUseCase: SaveDefaultTaskUseCase,
    @DefaultDispatcherQualifier private val defaultDispatcher: CoroutineDispatcher
) : BaseViewTasksViewModel<ViewTasksScreenEvent, ViewTasksScreenState, ViewTasksScreenNavigation, ViewTasksScreenConfig, TaskFilterByStatus.TaskStatus, TaskSort.TasksSort>(
    readTagsUseCase = readTagsUseCase,
    archiveTaskByIdUseCase = archiveTaskByIdUseCase,
    deleteTaskByIdUseCase = deleteTaskByIdUseCase,
    defaultDispatcher = defaultDispatcher
) {

    private val allTasksState = readFullTasksUseCase()

    private val allProcessedTasksState = combine(
        allTasksState,
        filterByTagsIdsState,
        filterByStatusState,
        sortState
    ) { allTasks, filterByTagsIds, filterByStatus, sort ->
        if (allTasks.isNotEmpty()) {
            withContext(defaultDispatcher) {
                UIResultModel.Data(
                    allTasks
                        .asSequence()
                        .let { if (filterByTagsIds.isNotEmpty()) it.filterByTags(filterByTagsIds) else it }
                        .let { if (filterByStatus != null) it.filterByStatus(filterByStatus) else it }
                        .sortHabits(sort)
                        .toList()
                )
            }
        } else UIResultModel.NoData
    }.stateIn(
        viewModelScope,
        SharingStarted.Eagerly,
        UIResultModel.Loading(null)
    )

    override val uiScreenState: StateFlow<ViewTasksScreenState> =
        combine(
            allProcessedTasksState,
            allSelectableTagsState,
            filterByStatusState,
            sortState
        ) { allProcessedTasks, allSelectableTags, filterByStatus, sort ->
            ViewTasksScreenState(
                allTasksResult = allProcessedTasks,
                allSelectableTags = allSelectableTags,
                filterByStatus = filterByStatus,
                sort = sort
            )
        }.stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            ViewTasksScreenState(
                allTasksResult = allProcessedTasksState.value,
                allSelectableTags = allSelectableTagsState.value,
                filterByStatus = filterByStatusState.value,
                sort = sortState.value
            )
        )

    override fun onEvent(event: ViewTasksScreenEvent) {
        when (event) {
            is ViewTasksScreenEvent.Base -> onBaseEvent(event.baseEvent)
            is ViewTasksScreenEvent.OnFilterByStatusClick ->
                onFilterByStatusClick(event)

            is ViewTasksScreenEvent.OnSortClick ->
                onSortClick(event)

            is ViewTasksScreenEvent.OnTaskClick ->
                onTaskClick(event)

            is ViewTasksScreenEvent.OnCreateTaskClick ->
                onCreateTaskClick()

            is ViewTasksScreenEvent.ResultEvent ->
                onResultEvent(event)
        }
    }

    private fun onResultEvent(event: ViewTasksScreenEvent.ResultEvent) {
        when (event) {
            is ViewTasksScreenEvent.ResultEvent.PickTaskType ->
                onPickTaskTypeResultEvent(event)
        }
    }

    private fun onPickTaskTypeResultEvent(event: ViewTasksScreenEvent.ResultEvent.PickTaskType) {
        onIdleToAction {
            when (val result = event.result) {
                is PickTaskTypeScreenResult.Confirm -> onConfirmPickTaskType(result)
                is PickTaskTypeScreenResult.Dismiss -> Unit
            }
        }
    }

    private fun onConfirmPickTaskType(result: PickTaskTypeScreenResult.Confirm) {
        viewModelScope.launch {
            val requestType = when (result.taskType) {
                TaskType.RecurringTask -> SaveDefaultTaskUseCase.RequestType.CreateRecurringTask
                TaskType.SingleTask -> SaveDefaultTaskUseCase.RequestType.CreateTask
                else -> throw IllegalStateException()
            }
            when (val resultModel = saveDefaultTaskUseCase(requestType)) {
                is ResultModel.Success -> {
                    val taskId = resultModel.data
                    setUpNavigationState(ViewTasksScreenNavigation.CreateTask(taskId))
                }
                is ResultModel.Error -> Unit
            }
        }
    }

    private fun onCreateTaskClick() {
        val availableTypes = setOf(TaskType.RecurringTask, TaskType.SingleTask)
        setUpConfigState(ViewTasksScreenConfig.PickTaskType(
            TaskType.entries.filter { it in availableTypes }
        ))
    }

    private fun onTaskClick(event: ViewTasksScreenEvent.OnTaskClick) {
        super.onEditTask(event.taskId)
    }

    private fun onFilterByStatusClick(event: ViewTasksScreenEvent.OnFilterByStatusClick) {
        super.onFilterByStatusClick(event.filter)
    }

    private fun onSortClick(event: ViewTasksScreenEvent.OnSortClick) {
        super.onSortClick(event.sort)
    }

    override fun setUpBaseNavigationState(baseSN: BaseViewTasksScreenNavigation) {
        setUpNavigationState(
            ViewTasksScreenNavigation.Base(baseSN)
        )
    }

    override fun setUpBaseConfigState(baseConfig: BaseViewTasksScreenConfig) {
        setUpConfigState(ViewTasksScreenConfig.Base(baseConfig))
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