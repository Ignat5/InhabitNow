package com.example.inhabitnow.android.presentation.view_activities.base

import androidx.lifecycle.viewModelScope
import com.example.inhabitnow.android.presentation.base.components.config.ScreenConfig
import com.example.inhabitnow.android.presentation.base.components.event.ScreenEvent
import com.example.inhabitnow.android.presentation.base.components.navigation.ScreenNavigation
import com.example.inhabitnow.android.presentation.base.components.state.ScreenState
import com.example.inhabitnow.android.presentation.base.view_model.BaseViewModel
import com.example.inhabitnow.android.presentation.create_edit_task.common.config.pick_tags.model.SelectableTagModel
import com.example.inhabitnow.android.presentation.create_edit_task.edit.config.confirm_archive.ConfirmArchiveTaskScreenResult
import com.example.inhabitnow.android.presentation.create_edit_task.edit.config.confirm_delete.ConfirmDeleteTaskScreenResult
import com.example.inhabitnow.android.presentation.view_activities.base.components.BaseViewTasksScreenConfig
import com.example.inhabitnow.android.presentation.view_activities.base.components.BaseViewTasksScreenEvent
import com.example.inhabitnow.android.presentation.view_activities.base.components.BaseViewTasksScreenNavigation
import com.example.inhabitnow.android.presentation.view_activities.model.TaskFilterByStatus
import com.example.inhabitnow.android.presentation.view_activities.model.TaskSort
import com.example.inhabitnow.domain.model.task.content.TaskContentModel
import com.example.inhabitnow.domain.model.task.derived.FullTaskModel
import com.example.inhabitnow.domain.use_case.archive_task_by_id.ArchiveTaskByIdUseCase
import com.example.inhabitnow.domain.use_case.delete_task_by_id.DeleteTaskByIdUseCase
import com.example.inhabitnow.domain.use_case.tag.read_tags.ReadTagsUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

abstract class BaseViewTasksViewModel<SE : ScreenEvent, SS : ScreenState, SN : ScreenNavigation, SC : ScreenConfig, TFS : TaskFilterByStatus, TS : TaskSort>(
    readTagsUseCase: ReadTagsUseCase,
    private val archiveTaskByIdUseCase: ArchiveTaskByIdUseCase,
    private val deleteTaskByIdUseCase: DeleteTaskByIdUseCase,
    private val defaultDispatcher: CoroutineDispatcher
) : BaseViewModel<SE, SS, SN, SC>() {
    private val todayDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date

    private val _filterByTagsIdsState = MutableStateFlow<Set<String>>(emptySet())
    protected val filterByTagsIdsState: StateFlow<Set<String>> = _filterByTagsIdsState

    private val _filterByStatusState = MutableStateFlow<TFS?>(null)
    protected val filterByStatusState: StateFlow<TFS?> = _filterByStatusState

    private val _sortState = MutableStateFlow<TS?>(null)
    protected val sortState: StateFlow<TS?> = _sortState

    private val allTagsState = readTagsUseCase()
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            emptyList()
        )

    protected val allSelectableTagsState = combine(
        allTagsState, filterByTagsIdsState
    ) { allTags, filterByTagsIds ->
        if (allTags.isNotEmpty()) {
            withContext(defaultDispatcher) {
                allTags.map { tagModel ->
                    SelectableTagModel(
                        tagModel = tagModel,
                        isSelected = tagModel.id in filterByTagsIds
                    )
                }
            }
        } else emptyList()
    }.stateIn(
        viewModelScope,
        SharingStarted.Eagerly,
        emptyList()
    )

    protected fun onBaseEvent(event: BaseViewTasksScreenEvent) {
        when (event) {
            is BaseViewTasksScreenEvent.OnTagClick ->
                onTagClick(event)

            is BaseViewTasksScreenEvent.ResultEvent ->
                onResultEvent(event)

            is BaseViewTasksScreenEvent.OnSearchClick ->
                onSearchClick()
        }
    }

    private fun onResultEvent(event: BaseViewTasksScreenEvent.ResultEvent) {
        when (event) {
            is BaseViewTasksScreenEvent.ResultEvent.ConfirmArchiveTask ->
                onConfirmArchiveTaskResultEvent(event)

            is BaseViewTasksScreenEvent.ResultEvent.ConfirmDeleteTask ->
                onConfirmDeleteTaskResultEvent(event)
        }
    }

    private fun onConfirmArchiveTaskResultEvent(event: BaseViewTasksScreenEvent.ResultEvent.ConfirmArchiveTask) {
        onIdleToAction {
            when (val result = event.result) {
                is ConfirmArchiveTaskScreenResult.Confirm -> onConfirmArchiveTask(result)
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

    private fun onConfirmDeleteTaskResultEvent(event: BaseViewTasksScreenEvent.ResultEvent.ConfirmDeleteTask) {
        onIdleToAction {
            when (val result = event.result) {
                is ConfirmDeleteTaskScreenResult.Confirm -> onConfirmDeleteTask(result)
                is ConfirmDeleteTaskScreenResult.Dismiss -> Unit
            }
        }
    }

    private fun onConfirmDeleteTask(result: ConfirmDeleteTaskScreenResult.Confirm) {
        viewModelScope.launch {
            deleteTaskByIdUseCase(result.taskId)
        }
    }

    private fun onTagClick(event: BaseViewTasksScreenEvent.OnTagClick) {
        val clickedTagId = event.tagId
        _filterByTagsIdsState.update { oldSet ->
            val newSet = mutableSetOf<String>()
            newSet.addAll(oldSet)
            if (newSet.contains(clickedTagId)) newSet.remove(clickedTagId)
            else newSet.add(clickedTagId)
            newSet
        }
    }

    private fun onSearchClick() {
        setUpBaseNavigationState(BaseViewTasksScreenNavigation.Search)
    }

    protected fun onEditTask(taskId: String) {
        setUpBaseNavigationState(BaseViewTasksScreenNavigation.EditTask(taskId))
    }

    protected fun onArchiveTask(taskId: String) {
        setUpBaseConfigState(BaseViewTasksScreenConfig.ConfirmArchiveTask(taskId))
    }

    protected fun onUnarchiveTask(taskId: String) {
        viewModelScope.launch {
            archiveTaskByIdUseCase(
                taskId = taskId,
                archive = false
            )
        }
    }

    protected fun onDeleteTask(taskId: String) {
        setUpBaseConfigState(BaseViewTasksScreenConfig.ConfirmDeleteTask(taskId))
    }

    protected fun onSortClick(sort: TS) {
        _sortState.update { oldState ->
            if (sort == oldState) null
            else sort
        }
    }

    protected fun onFilterByStatusClick(filterByStatus: TFS) {
        _filterByStatusState.update { oldFilter ->
            if (filterByStatus == oldFilter) null
            else filterByStatus
        }
    }

    protected abstract fun setUpBaseNavigationState(baseSN: BaseViewTasksScreenNavigation)
    protected abstract fun setUpBaseConfigState(baseConfig: BaseViewTasksScreenConfig)

    protected fun <T : FullTaskModel> Sequence<T>.filterByTags(
        filterByTagsIds: Set<String>
    ): Sequence<T> = this.let { allTasks ->
        if (filterByTagsIds.isEmpty()) allTasks
        else allTasks.filter { fullTask ->
            fullTask.allTags.any { it.id in filterByTagsIds }
        }
    }

    protected fun <T : FullTaskModel> Sequence<T>.filterByOnlyActive() = this.let { allTasks ->
        allTasks.filter { fullTask ->
            val inDatePeriod = when (val dc = fullTask.taskModel.dateContent) {
                is TaskContentModel.DateContent.Day -> {
                    dc.date <= todayDate
                }

                is TaskContentModel.DateContent.Period -> {
                    dc.startDate <= todayDate &&
                            (dc.endDate?.let { it >= todayDate } ?: true)
                }
            }
            !fullTask.taskModel.isArchived && inDatePeriod
        }
    }

    protected fun <T : FullTaskModel> Sequence<T>.filterByOnlyArchived() = this.let { allTasks ->
        allTasks.filter { it.taskModel.isArchived }
    }

    protected fun <T : FullTaskModel> Sequence<T>.sortByStartDate() = this.let { allTasks ->
        allTasks.sortedBy {
            when (val dc = it.taskModel.dateContent) {
                is TaskContentModel.DateContent.Day -> {
                    dc.date
                }

                is TaskContentModel.DateContent.Period -> {
                    dc.startDate
                }
            }
        }
    }

    protected fun <T : FullTaskModel> Sequence<T>.sortByPriority() = this.let { allTasks ->
        allTasks.sortedByDescending { it.taskModel.priority }
    }

    protected fun <T : FullTaskModel> Sequence<T>.sortByTitle() = this.let { allTasks ->
        allTasks.sortedBy { it.taskModel.title }
    }

    protected fun <T : FullTaskModel> Sequence<T>.sortByDefault() = this.let { allTasks ->
        allTasks.sortedByDescending { it.taskModel.createdAt }
    }


}