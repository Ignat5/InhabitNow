package com.example.inhabitnow.android.presentation.view_activities.view_habits

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.inhabitnow.android.core.di.qualifier.DefaultDispatcherQualifier
import com.example.inhabitnow.android.presentation.base.view_model.BaseViewModel
import com.example.inhabitnow.android.presentation.create_edit_task.edit.config.confirm_archive.ConfirmArchiveTaskScreenResult
import com.example.inhabitnow.android.presentation.create_edit_task.edit.config.confirm_delete.ConfirmDeleteTaskScreenResult
import com.example.inhabitnow.android.presentation.model.UIResultModel
import com.example.inhabitnow.android.presentation.view_activities.model.ItemTaskAction
import com.example.inhabitnow.android.presentation.view_activities.model.TaskFilterByStatus
import com.example.inhabitnow.android.presentation.view_activities.model.TaskSort
import com.example.inhabitnow.android.presentation.view_activities.view_habits.components.ViewHabitsScreenConfig
import com.example.inhabitnow.android.presentation.view_activities.view_habits.components.ViewHabitsScreenEvent
import com.example.inhabitnow.android.presentation.view_activities.view_habits.components.ViewHabitsScreenNavigation
import com.example.inhabitnow.android.presentation.view_activities.view_habits.components.ViewHabitsScreenState
import com.example.inhabitnow.android.presentation.view_activities.view_habits.config.view_habit_actions.ViewHabitActionsStateHolder
import com.example.inhabitnow.android.presentation.view_activities.view_habits.config.view_habit_actions.components.ViewHabitActionsScreenResult
import com.example.inhabitnow.domain.model.task.derived.FullTaskModel
import com.example.inhabitnow.domain.use_case.archive_task_by_id.ArchiveTaskByIdUseCase
import com.example.inhabitnow.domain.use_case.delete_task_by_id.DeleteTaskByIdUseCase
import com.example.inhabitnow.domain.use_case.read_full_habits.ReadFullHabitsUseCase
import com.example.inhabitnow.domain.use_case.tag.read_tags.ReadTagsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ViewHabitsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    readFullHabitsUseCase: ReadFullHabitsUseCase,
    readTagsUseCase: ReadTagsUseCase,
    private val archiveTaskByIdUseCase: ArchiveTaskByIdUseCase,
    private val deleteTaskByIdUseCase: DeleteTaskByIdUseCase,
    @DefaultDispatcherQualifier private val defaultDispatcher: CoroutineDispatcher
) : BaseViewModel<ViewHabitsScreenEvent, ViewHabitsScreenState, ViewHabitsScreenNavigation, ViewHabitsScreenConfig>() {
    private val filterByTagsIdsState = MutableStateFlow<Set<String>>(emptySet())
    private val filterByStatusState = MutableStateFlow<TaskFilterByStatus.HabitStatus?>(null)
    private val sortState = MutableStateFlow<TaskSort.HabitsSort?>(null)
    private val allHabitsState = readFullHabitsUseCase()
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            emptyList()
        )
    private val allProcessedHabitsState = combine(
        allHabitsState,
        filterByTagsIdsState,
        filterByStatusState,
        sortState
    ) { allHabits, filterByTagsIds, filterByStatus, sort ->
        if (allHabits.isNotEmpty()) {
            withContext(defaultDispatcher) {
                UIResultModel.Data(
                    allHabits
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

    private val allTagsState = readTagsUseCase()
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            emptyList()
        )

    override val uiScreenState: StateFlow<ViewHabitsScreenState> =
        combine(
            allProcessedHabitsState,
            allTagsState,
            filterByTagsIdsState,
            filterByStatusState,
            sortState
        ) { allProcessedHabits, allTags, filterByTagsIds, filterByStatus, sort ->
            ViewHabitsScreenState(
                allHabits = allProcessedHabits,
                allTags = allTags,
                filterByTagsIds = filterByTagsIds,
                filterByStatus = filterByStatus,
                sort = sort
            )
        }.stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            ViewHabitsScreenState(
                allHabits = allProcessedHabitsState.value,
                allTags = allTagsState.value,
                filterByTagsIds = filterByTagsIdsState.value,
                filterByStatus = filterByStatusState.value,
                sort = sortState.value
            )
        )

    override fun onEvent(event: ViewHabitsScreenEvent) {
        when (event) {
            is ViewHabitsScreenEvent.OnHabitClick ->
                onHabitClick(event)

            is ViewHabitsScreenEvent.ResultEvent ->
                onResultEvent(event)

            is ViewHabitsScreenEvent.OnFilterTagClick ->
                onFilterTagClick(event)

            is ViewHabitsScreenEvent.OnFilterByStatusClick ->
                onFilterByStatusClick(event)

            is ViewHabitsScreenEvent.OnSortClick ->
                onSortClick(event)

            is ViewHabitsScreenEvent.OnSearchTasksClick ->
                onSearchTasksClick()
        }
    }

    private fun onResultEvent(event: ViewHabitsScreenEvent.ResultEvent) {
        when (event) {
            is ViewHabitsScreenEvent.ResultEvent.ViewHabitActions ->
                onViewHabitActionsResult(event)

            is ViewHabitsScreenEvent.ResultEvent.ConfirmArchiveTask ->
                onConfirmArchiveTaskResultEvent(event)

            is ViewHabitsScreenEvent.ResultEvent.ConfirmDeleteTask ->
                onConfirmDeleteTaskResultEvent(event)
        }
    }

    private fun onConfirmArchiveTaskResultEvent(event: ViewHabitsScreenEvent.ResultEvent.ConfirmArchiveTask) {
        onIdleToAction {
            when (val result = event.result) {
                is ConfirmArchiveTaskScreenResult.Confirm ->
                    onConfirmArchiveTask(result)

                is ConfirmArchiveTaskScreenResult.Dismiss -> Unit
            }
        }
    }

    private fun onConfirmArchiveTask(result: ConfirmArchiveTaskScreenResult.Confirm) {
        viewModelScope.launch {
            archiveTaskByIdUseCase(
                taskId = result.taskId,
                archive = true
            )
        }
    }

    private fun onConfirmDeleteTaskResultEvent(event: ViewHabitsScreenEvent.ResultEvent.ConfirmDeleteTask) {
        onIdleToAction {
            when (val result = event.result) {
                is ConfirmDeleteTaskScreenResult.Confirm ->
                    onConfirmDeleteTask(result)

                is ConfirmDeleteTaskScreenResult.Dismiss -> Unit
            }
        }
    }

    private fun onConfirmDeleteTask(result: ConfirmDeleteTaskScreenResult.Confirm) {
        viewModelScope.launch {
            deleteTaskByIdUseCase(result.taskId)
        }
    }

    private fun onViewHabitActionsResult(event: ViewHabitsScreenEvent.ResultEvent.ViewHabitActions) {
        onIdleToAction {
            when (val result = event.result) {
                is ViewHabitActionsScreenResult.Action ->
                    onHabitActionResult(result)

                is ViewHabitActionsScreenResult.Edit ->
                    onEditResult(result)

                is ViewHabitActionsScreenResult.Dismiss -> Unit
            }
        }
    }

    private fun onHabitActionResult(result: ViewHabitActionsScreenResult.Action) {
        when (val action = result.action) {
            is ItemTaskAction.ViewStatistics ->
                onViewStatisticsAction(result.taskId)

            is ItemTaskAction.ArchiveUnarchive -> {
                when (action) {
                    is ItemTaskAction.ArchiveUnarchive.Archive ->
                        onArchiveAction(result.taskId)

                    is ItemTaskAction.ArchiveUnarchive.Unarchive ->
                        onUnarchiveAction(result.taskId)
                }
            }

            is ItemTaskAction.Delete ->
                onDeleteAction(result.taskId)
        }
    }

    private fun onViewStatisticsAction(taskId: String) {
        /* TODO */
    }

    private fun onArchiveAction(taskId: String) {
        setUpConfigState(ViewHabitsScreenConfig.ConfirmArchiveTask(taskId))
    }

    private fun onUnarchiveAction(taskId: String) {
        viewModelScope.launch {
            archiveTaskByIdUseCase(
                taskId = taskId,
                archive = false
            )
        }
    }

    private fun onDeleteAction(taskId: String) {
        setUpConfigState(ViewHabitsScreenConfig.ConfirmDeleteTask(taskId))
    }

    private fun onEditResult(result: ViewHabitActionsScreenResult.Edit) {
        setUpNavigationState(ViewHabitsScreenNavigation.EditTask(result.taskId))
    }

    private fun onHabitClick(event: ViewHabitsScreenEvent.OnHabitClick) {
        allHabitsState.value.find { it.taskModel.id == event.taskId }?.let { fullHabit ->
            setUpConfigState(
                ViewHabitsScreenConfig.ViewHabitActions(
                    stateHolder = ViewHabitActionsStateHolder(
                        habitModel = fullHabit.taskModel,
                        holderScope = provideChildScope()
                    )
                )
            )
        }
    }

    private fun onFilterTagClick(event: ViewHabitsScreenEvent.OnFilterTagClick) {
        val clickedTagId = event.tagId
        filterByTagsIdsState.update { oldSet ->
            val newSet = mutableSetOf<String>()
            newSet.addAll(oldSet)
            if (newSet.contains(clickedTagId)) {
                newSet.remove(clickedTagId)
            } else newSet.add(clickedTagId)
            newSet
        }
    }

    private fun onFilterByStatusClick(event: ViewHabitsScreenEvent.OnFilterByStatusClick) {
        val clickedFilter = event.filterByStatus
        val currentFilter = filterByStatusState.value
        filterByStatusState.update {
            if (clickedFilter != currentFilter) {
                clickedFilter
            } else null
        }
    }

    private fun onSortClick(event: ViewHabitsScreenEvent.OnSortClick) {
        val clickedSort = event.sort
        val currentSort = sortState.value
        sortState.update {
            if (clickedSort != currentSort) {
                clickedSort
            } else null
        }
    }

    private fun onSearchTasksClick() {
        setUpNavigationState(ViewHabitsScreenNavigation.Search)
    }

    private fun Sequence<FullTaskModel.FullHabit>.filterByTags(
        filterByTagsIds: Set<String>
    ): Sequence<FullTaskModel.FullHabit> = this.let { allHabits ->
        if (filterByTagsIds.isEmpty()) allHabits
        else allHabits.filter { fullHabit ->
            fullHabit.allTags.any { it.id in filterByTagsIds }
        }
    }

    private fun Sequence<FullTaskModel.FullHabit>.filterByStatus(
        filterByStatus: TaskFilterByStatus.HabitStatus
    ): Sequence<FullTaskModel.FullHabit> = this.let { allHabits ->
        when (filterByStatus) {
            is TaskFilterByStatus.OnlyActive -> {
                allHabits.filter { fullHabit ->
                    !fullHabit.taskModel.isArchived
                }
            }

            is TaskFilterByStatus.OnlyArchived -> {
                allHabits.filter { fullHabit ->
                    fullHabit.taskModel.isArchived
                }
            }
        }
    }

    private fun Sequence<FullTaskModel.FullHabit>.sortHabits(
        sort: TaskSort.HabitsSort?
    ): Sequence<FullTaskModel.FullHabit> = this.let { allHabits ->
        when (sort) {
            is TaskSort.ByStartDate -> {
                allHabits.sortedByDescending { it.taskModel.dateContent.startDate }
            }

            is TaskSort.ByPriority -> {
                allHabits.sortedByDescending { it.taskModel.priority }
            }

            is TaskSort.ByTitle -> {
                allHabits.sortedBy { it.taskModel.title }
            }

            null -> {
                allHabits.sortedByDescending { it.taskModel.createdAt }
            }
        }
    }

}