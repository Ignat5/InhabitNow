package com.example.inhabitnow.android.presentation.view_activities.view_habits

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.inhabitnow.android.core.di.qualifier.DefaultDispatcherQualifier
import com.example.inhabitnow.android.presentation.base.view_model.BaseViewModel
import com.example.inhabitnow.android.presentation.create_edit_task.edit.config.confirm_archive.ConfirmArchiveTaskScreenResult
import com.example.inhabitnow.android.presentation.create_edit_task.edit.config.confirm_delete.ConfirmDeleteTaskScreenResult
import com.example.inhabitnow.android.presentation.model.UIResultModel
import com.example.inhabitnow.android.presentation.view_activities.base.BaseViewTasksViewModel
import com.example.inhabitnow.android.presentation.view_activities.base.components.BaseViewTasksScreenConfig
import com.example.inhabitnow.android.presentation.view_activities.base.components.BaseViewTasksScreenNavigation
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
    archiveTaskByIdUseCase: ArchiveTaskByIdUseCase,
    deleteTaskByIdUseCase: DeleteTaskByIdUseCase,
    @DefaultDispatcherQualifier private val defaultDispatcher: CoroutineDispatcher
) : BaseViewTasksViewModel<ViewHabitsScreenEvent, ViewHabitsScreenState, ViewHabitsScreenNavigation, ViewHabitsScreenConfig, TaskFilterByStatus.HabitStatus, TaskSort.HabitsSort>(
    readTagsUseCase = readTagsUseCase,
    archiveTaskByIdUseCase = archiveTaskByIdUseCase,
    deleteTaskByIdUseCase = deleteTaskByIdUseCase,
    defaultDispatcher = defaultDispatcher
) {
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

    override val uiScreenState: StateFlow<ViewHabitsScreenState> =
        combine(
            allProcessedHabitsState,
            allSelectableTagsState,
            filterByStatusState,
            sortState
        ) { allProcessedHabits, allSelectableTags, filterByStatus, sort ->
            ViewHabitsScreenState(
                allTasksResult = allProcessedHabits,
                allSelectableTags = allSelectableTags,
                filterByStatus = filterByStatus,
                sort = sort
            )
        }.stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            ViewHabitsScreenState(
                allTasksResult = allProcessedHabitsState.value,
                allSelectableTags = allSelectableTagsState.value,
                filterByStatus = filterByStatusState.value,
                sort = sortState.value
            )
        )

    override fun onEvent(event: ViewHabitsScreenEvent) {
        when (event) {
            is ViewHabitsScreenEvent.Base ->
                onBaseEvent(event.baseEvent)

            is ViewHabitsScreenEvent.OnHabitClick ->
                onHabitClick(event)

            is ViewHabitsScreenEvent.ResultEvent ->
                onResultEvent(event)

            is ViewHabitsScreenEvent.OnFilterByStatusClick ->
                onFilterByStatusClick(event)

            is ViewHabitsScreenEvent.OnSortClick ->
                onSortClick(event)

        }
    }

    private fun onResultEvent(event: ViewHabitsScreenEvent.ResultEvent) {
        when (event) {
            is ViewHabitsScreenEvent.ResultEvent.ViewHabitActions ->
                onViewHabitActionsResult(event)
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
        super.onArchiveTask(taskId)
    }

    private fun onUnarchiveAction(taskId: String) {
        super.onUnarchiveTask(taskId)
    }

    private fun onDeleteAction(taskId: String) {
        super.onDeleteTask(taskId)
    }

    private fun onEditResult(result: ViewHabitActionsScreenResult.Edit) {
        super.onEditTask(result.taskId)
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

    private fun onFilterByStatusClick(event: ViewHabitsScreenEvent.OnFilterByStatusClick) {
        super.onFilterByStatusClick(event.filterByStatus)
    }

    private fun onSortClick(event: ViewHabitsScreenEvent.OnSortClick) {
        super.onSortClick(event.sort)
    }

    override fun setUpBaseConfigState(baseConfig: BaseViewTasksScreenConfig) {
        setUpConfigState(ViewHabitsScreenConfig.Base(baseConfig))
    }

    override fun setUpBaseNavigationState(baseSN: BaseViewTasksScreenNavigation) {
        setUpNavigationState(ViewHabitsScreenNavigation.Base(baseSN))
    }

    private fun Sequence<FullTaskModel.FullHabit>.filterByStatus(
        filterByStatus: TaskFilterByStatus.HabitStatus
    ): Sequence<FullTaskModel.FullHabit> = this.let { allHabits ->
        when (filterByStatus) {
            is TaskFilterByStatus.OnlyActive -> allHabits.filterByOnlyActive()
            is TaskFilterByStatus.OnlyArchived -> allHabits.filterByOnlyArchived()
        }
    }

    private fun Sequence<FullTaskModel.FullHabit>.sortHabits(
        sort: TaskSort.HabitsSort?
    ): Sequence<FullTaskModel.FullHabit> = this.let { allHabits ->
        when (sort) {
            null -> allHabits.sortByDefault()
            is TaskSort.ByStartDate -> allHabits.sortByStartDate()
            is TaskSort.ByPriority -> allHabits.sortByPriority()
            is TaskSort.ByTitle -> allHabits.sortByTitle()
        }
    }

}