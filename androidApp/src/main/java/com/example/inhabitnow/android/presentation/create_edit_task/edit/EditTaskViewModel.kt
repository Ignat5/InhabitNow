package com.example.inhabitnow.android.presentation.create_edit_task.edit

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.inhabitnow.android.core.di.qualifier.DefaultDispatcherQualifier
import com.example.inhabitnow.android.navigation.AppNavDest
import com.example.inhabitnow.android.presentation.create_edit_task.base.BaseCreateEditTaskViewModel
import com.example.inhabitnow.android.presentation.create_edit_task.base.components.BaseCreateEditTaskScreenConfig
import com.example.inhabitnow.android.presentation.create_edit_task.edit.components.EditTaskScreenConfig
import com.example.inhabitnow.android.presentation.create_edit_task.edit.components.EditTaskScreenEvent
import com.example.inhabitnow.android.presentation.create_edit_task.edit.components.EditTaskScreenNavigation
import com.example.inhabitnow.android.presentation.create_edit_task.edit.components.EditTaskScreenState
import com.example.inhabitnow.android.presentation.create_edit_task.edit.model.ItemTaskAction
import com.example.inhabitnow.core.type.TaskType
import com.example.inhabitnow.domain.use_case.read_task_with_content_by_id.ReadTaskWithContentByIdUseCase
import com.example.inhabitnow.domain.use_case.reminder.read_reminders_count_by_task_id.ReadRemindersCountByTaskIdUseCase
import com.example.inhabitnow.domain.use_case.save_task_by_id.SaveTaskByIdUseCase
import com.example.inhabitnow.domain.use_case.tag.read_tag_ids_by_task_id.ReadTagIdsByTaskIdUseCase
import com.example.inhabitnow.domain.use_case.tag.read_tags.ReadTagsUseCase
import com.example.inhabitnow.domain.use_case.tag.save_tag_cross_by_task_id.SaveTagCrossByTaskIdUseCase
import com.example.inhabitnow.domain.use_case.update_task_date.UpdateTaskDateUseCase
import com.example.inhabitnow.domain.use_case.update_task_description.UpdateTaskDescriptionByIdUseCase
import com.example.inhabitnow.domain.use_case.update_task_frequency_by_id.UpdateTaskFrequencyByIdUseCase
import com.example.inhabitnow.domain.use_case.update_task_priority_by_id.UpdateTaskPriorityByIdUseCase
import com.example.inhabitnow.domain.use_case.update_task_progress_by_id.UpdateTaskProgressByIdUseCase
import com.example.inhabitnow.domain.use_case.update_task_title_by_id.UpdateTaskTitleByIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class EditTaskViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val readTaskWithContentByIdUseCase: ReadTaskWithContentByIdUseCase,
    private val readRemindersCountByTaskIdUseCase: ReadRemindersCountByTaskIdUseCase,
    private val readTagsUseCase: ReadTagsUseCase,
    private val readTagIdsByTaskIdUseCase: ReadTagIdsByTaskIdUseCase,
    private val updateTaskTitleByIdUseCase: UpdateTaskTitleByIdUseCase,
    private val updateTaskProgressByIdUseCase: UpdateTaskProgressByIdUseCase,
    private val updateTaskFrequencyByIdUseCase: UpdateTaskFrequencyByIdUseCase,
    private val updateTaskDateUseCase: UpdateTaskDateUseCase,
    private val updateTaskDescriptionByIdUseCase: UpdateTaskDescriptionByIdUseCase,
    private val updateTaskPriorityByIdUseCase: UpdateTaskPriorityByIdUseCase,
    private val saveTagCrossByTaskIdUseCase: SaveTagCrossByTaskIdUseCase,
    private val saveTaskByIdUseCase: SaveTaskByIdUseCase,
    @DefaultDispatcherQualifier private val defaultDispatcher: CoroutineDispatcher,
) : BaseCreateEditTaskViewModel<EditTaskScreenEvent, EditTaskScreenState, EditTaskScreenNavigation, EditTaskScreenConfig>(
    taskId = checkNotNull(savedStateHandle.get<String>(AppNavDest.TASK_ID_KEY)),
    readTaskWithContentByIdUseCase = readTaskWithContentByIdUseCase,
    readRemindersCountByTaskIdUseCase = readRemindersCountByTaskIdUseCase,
    readTagsUseCase = readTagsUseCase,
    readTagIdsByTaskIdUseCase = readTagIdsByTaskIdUseCase,
    updateTaskTitleByIdUseCase = updateTaskTitleByIdUseCase,
    updateTaskDescriptionByIdUseCase = updateTaskDescriptionByIdUseCase,
    updateTaskProgressByIdUseCase = updateTaskProgressByIdUseCase,
    updateTaskFrequencyByIdUseCase = updateTaskFrequencyByIdUseCase,
    updateTaskDateUseCase = updateTaskDateUseCase,
    defaultDispatcher = defaultDispatcher
) {

    private val allTaskActionItemsState = taskWithContentState.map { taskWithContent ->
        if (taskWithContent != null) {
            mutableListOf<ItemTaskAction>().apply {
                if (taskWithContent.task.type == TaskType.Habit) {
                    add(ItemTaskAction.ViewStatistics)
                    add(ItemTaskAction.RestartHabit)
                }
                add(
                    if (taskWithContent.archiveContent.isArchived) ItemTaskAction.ArchiveUnarchive.Unarchive
                    else ItemTaskAction.ArchiveUnarchive.Archive
                )
                add(ItemTaskAction.DeleteTask)
            }
        } else emptyList()
    }.stateIn(
        viewModelScope,
        SharingStarted.Eagerly,
        emptyList()
    )

    override val uiScreenState: StateFlow<EditTaskScreenState> =
        combine(
            allTaskConfigItemsState,
            allTaskActionItemsState
        ) { allTaskConfigItems, allTaskActionItems ->
            EditTaskScreenState(
                allTaskConfigItems = allTaskConfigItems,
                allTaskActionItems = allTaskActionItems
            )
        }.stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            EditTaskScreenState(
                allTaskConfigItems = allTaskConfigItemsState.value,
                allTaskActionItems = allTaskActionItemsState.value
            )
        )

    override fun onEvent(event: EditTaskScreenEvent) {
        when (event) {
            is EditTaskScreenEvent.BaseEvent -> onBaseEvent(event.baseEvent)
            else -> {
                /* TODO */
            }
        }
    }

    override fun setUpBaseConfigState(baseConfig: BaseCreateEditTaskScreenConfig) {
        setUpConfigState(EditTaskScreenConfig.BaseConfig(baseConfig))
    }

}