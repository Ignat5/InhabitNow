package com.example.inhabitnow.android.presentation.create_edit_task.base

import androidx.lifecycle.viewModelScope
import com.example.inhabitnow.android.presentation.base.components.config.ScreenConfig
import com.example.inhabitnow.android.presentation.base.components.event.ScreenEvent
import com.example.inhabitnow.android.presentation.base.components.navigation.ScreenNavigation
import com.example.inhabitnow.android.presentation.base.components.state.ScreenState
import com.example.inhabitnow.android.presentation.base.view_model.BaseViewModel
import com.example.inhabitnow.android.presentation.create_edit_task.base.components.BaseCreateEditTaskScreenConfig
import com.example.inhabitnow.android.presentation.create_edit_task.base.components.BaseCreateEditTaskScreenEvent
import com.example.inhabitnow.android.presentation.create_edit_task.base.components.BaseCreateEditTaskScreenState
import com.example.inhabitnow.android.presentation.create_edit_task.common.config.model.BaseItemTaskConfig
import com.example.inhabitnow.android.presentation.create_edit_task.common.config.pick_description.PickTaskDescriptionStateHolder
import com.example.inhabitnow.android.presentation.create_edit_task.common.config.pick_description.components.PickTaskDescriptionScreenResult
import com.example.inhabitnow.android.presentation.create_edit_task.common.config.pick_task_title.PickTaskTitleStateHolder
import com.example.inhabitnow.android.presentation.create_edit_task.common.config.pick_task_title.components.PickTaskTitleScreenResult
import com.example.inhabitnow.android.presentation.model.UITaskContent
import com.example.inhabitnow.android.ui.toUIDateContent
import com.example.inhabitnow.android.ui.toUIFrequencyContent
import com.example.inhabitnow.android.ui.toUIProgressContent
import com.example.inhabitnow.domain.model.task.TaskWithContentModel
import com.example.inhabitnow.domain.use_case.read_task_with_content_by_id.ReadTaskWithContentByIdUseCase
import com.example.inhabitnow.domain.use_case.reminder.read_reminders_count_by_task_id.ReadRemindersCountByTaskIdUseCase
import com.example.inhabitnow.domain.use_case.tag.read_tag_ids_by_task_id.ReadTagIdsByTaskIdUseCase
import com.example.inhabitnow.domain.use_case.tag.read_tags.ReadTagsUseCase
import com.example.inhabitnow.domain.use_case.update_task_description.UpdateTaskDescriptionByIdUseCase
import com.example.inhabitnow.domain.use_case.update_task_title_by_id.UpdateTaskTitleByIdUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

abstract class BaseCreateEditTaskViewModel<SE : ScreenEvent, SS : ScreenState, SN : ScreenNavigation, SC : ScreenConfig>(
    protected val taskId: String,
    readTaskWithContentByIdUseCase: ReadTaskWithContentByIdUseCase,
    readRemindersCountByTaskIdUseCase: ReadRemindersCountByTaskIdUseCase,
    readTagsUseCase: ReadTagsUseCase,
    readTagIdsByTaskIdUseCase: ReadTagIdsByTaskIdUseCase,
    private val updateTaskTitleByIdUseCase: UpdateTaskTitleByIdUseCase,
    private val updateTaskDescriptionByIdUseCase: UpdateTaskDescriptionByIdUseCase,
    private val defaultDispatcher: CoroutineDispatcher
) : BaseViewModel<SE, SS, SN, SC>() {

    protected val taskWithContentState = readTaskWithContentByIdUseCase(taskId)
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            null
        )

    private val taskRemindersCountState = readRemindersCountByTaskIdUseCase(taskId)
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            DEFAULT_REMINDER_COUNT
        )

    private val allTagsState = readTagsUseCase()
        .map { it.sortedBy { tag -> tag.createdAt } }
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            emptyList()
        )

    private val taskTagIdsState: StateFlow<Set<String>> = readTagIdsByTaskIdUseCase(taskId)
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            emptySet()
        )

    private val taskTagsCountState = taskTagIdsState
        .map { it.size }
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            DEFAULT_TAG_COUNT
        )

    protected val allTaskConfigItemsState = combine(
        taskWithContentState,
        taskRemindersCountState,
        taskTagsCountState
    ) { taskWithContent, taskRemindersCount, taskTagsCount ->
        provideBaseTaskConfigItems(
            taskWithContentModel = taskWithContent,
            taskRemindersCount = taskRemindersCount,
            taskTagsCount = taskTagsCount
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.Eagerly,
        emptyList()
    )

    protected fun onBaseEvent(event: BaseCreateEditTaskScreenEvent) {
        when (event) {
            is BaseCreateEditTaskScreenEvent.OnBaseItemTaskConfigClick ->
                onBaseItemTaskConfigClick(event)

            is BaseCreateEditTaskScreenEvent.ResultEvent ->
                onResultEvent(event)

        }
    }

    private fun onBaseItemTaskConfigClick(event: BaseCreateEditTaskScreenEvent.OnBaseItemTaskConfigClick) {
        when (event.item) {
            is BaseItemTaskConfig.Title -> onConfigTaskTitleClick()
            is BaseItemTaskConfig.Description -> onConfigTaskDescriptionClick()
            else -> {
                /* TODO */
            }
        }
    }

    private fun onConfigTaskDescriptionClick() {
        taskWithContentState.value?.task?.description?.let { description ->
            setUpBaseConfigState(
                BaseCreateEditTaskScreenConfig.PickTaskDescription(
                    stateHolder = PickTaskDescriptionStateHolder(
                        initDescription = description,
                        holderScope = provideChildScope()
                    )
                )
            )
        }
    }

    private fun onConfigTaskTitleClick() {
        taskWithContentState.value?.task?.title?.let { title ->
            setUpBaseConfigState(
                BaseCreateEditTaskScreenConfig.PickTaskTitle(
                    stateHolder = PickTaskTitleStateHolder(
                        initTitle = title,
                        holderScope = provideChildScope()
                    )
                )
            )
        }
    }

    private fun onResultEvent(event: BaseCreateEditTaskScreenEvent.ResultEvent) {
        when (event) {
            is BaseCreateEditTaskScreenEvent.ResultEvent.PickTaskTitle ->
                onPickTaskTitleResultEvent(event)

            is BaseCreateEditTaskScreenEvent.ResultEvent.PickTaskDescription ->
                onPickTaskDescriptionResultEvent(event)
        }
    }

    private fun onPickTaskDescriptionResultEvent(event: BaseCreateEditTaskScreenEvent.ResultEvent.PickTaskDescription) {
        onIdleToAction {
            when (val result = event.result) {
                is PickTaskDescriptionScreenResult.Confirm -> onConfirmPickTaskDescription(result)
                is PickTaskDescriptionScreenResult.Dismiss -> Unit
            }
        }
    }

    private fun onConfirmPickTaskDescription(result: PickTaskDescriptionScreenResult.Confirm) {
        viewModelScope.launch {
            updateTaskDescriptionByIdUseCase(
                taskId = taskId,
                description = result.description
            )
        }
    }

    private fun onPickTaskTitleResultEvent(event: BaseCreateEditTaskScreenEvent.ResultEvent.PickTaskTitle) {
        onIdleToAction {
            when (val result = event.result) {
                is PickTaskTitleScreenResult.Confirm -> onConfirmPickTaskTitle(result)
                is PickTaskTitleScreenResult.Dismiss -> Unit
            }
        }
    }

    private fun onConfirmPickTaskTitle(result: PickTaskTitleScreenResult.Confirm) {
        viewModelScope.launch {
            updateTaskTitleByIdUseCase(
                taskId = taskId,
                title = result.title
            )
        }
    }

    protected abstract fun setUpBaseConfigState(baseConfig: BaseCreateEditTaskScreenConfig)

    private suspend fun provideBaseTaskConfigItems(
        taskWithContentModel: TaskWithContentModel?,
        taskRemindersCount: Int,
        taskTagsCount: Int
    ): List<BaseItemTaskConfig> =
        withContext(defaultDispatcher) {
            if (taskWithContentModel != null) {
                mutableListOf<BaseItemTaskConfig>().apply {
                    add(BaseItemTaskConfig.Title(taskWithContentModel.task.title))
                    add(BaseItemTaskConfig.Description(taskWithContentModel.task.description))
                    when (val pc = taskWithContentModel.progressContent.toUIProgressContent()) {
                        is UITaskContent.Progress.Number -> {
                            add(
                                BaseItemTaskConfig.Progress.Number(pc)
                            )
                        }

                        is UITaskContent.Progress.Time -> {
                            add(
                                BaseItemTaskConfig.Progress.Time(pc)
                            )
                        }

                        else -> Unit
                    }

                    when (val fc = taskWithContentModel.frequencyContent.toUIFrequencyContent()) {
                        is UITaskContent.Frequency -> {
                            add(
                                BaseItemTaskConfig.Frequency(fc)
                            )
                        }

                        else -> Unit
                    }

                    when (val dc = taskWithContentModel.task.toUIDateContent()) {
                        is UITaskContent.Date.OneDay -> add(
                            BaseItemTaskConfig.Date.OneDayDate(dc.date)
                        )

                        is UITaskContent.Date.Period -> {
                            add(BaseItemTaskConfig.Date.StartDate(dc.startDate))
                            add(BaseItemTaskConfig.Date.EndDate(dc.endDate))
                        }
                    }

                    add(BaseItemTaskConfig.Reminders(taskRemindersCount))
                    add(BaseItemTaskConfig.Tags(taskTagsCount))
                    add(BaseItemTaskConfig.Priority(taskWithContentModel.task.priority))

                }.sortedBy { it.key.ordinal }
            } else emptyList()
        }

    companion object {
        private const val DEFAULT_REMINDER_COUNT = 0
        private const val DEFAULT_TAG_COUNT = 0
    }

}