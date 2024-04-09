package com.example.inhabitnow.android.presentation.main
//
//import androidx.lifecycle.SavedStateHandle
//import androidx.lifecycle.viewModelScope
//import com.example.inhabitnow.android.presentation.base.components.config.BaseConfigState
//import com.example.inhabitnow.android.presentation.base.view_model.BaseViewModel
//import com.example.inhabitnow.android.presentation.main.components.MainScreenConfig
//import com.example.inhabitnow.android.presentation.main.components.MainScreenEvent
//import com.example.inhabitnow.android.presentation.main.components.MainScreenNavigation
//import com.example.inhabitnow.android.presentation.main.components.MainScreenState
//import com.example.inhabitnow.android.presentation.main.config.pick_task_progress_type.PickTaskProgressTypeScreenResult
//import com.example.inhabitnow.android.presentation.main.config.pick_task_type.PickTaskTypeScreenResult
//import com.example.inhabitnow.core.model.ResultModel
//import com.example.inhabitnow.core.type.TaskProgressType
//import com.example.inhabitnow.core.type.TaskType
//import com.example.inhabitnow.domain.use_case.save_default_task.SaveDefaultTaskUseCase
//import dagger.hilt.android.lifecycle.HiltViewModel
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.SharingStarted
//import kotlinx.coroutines.flow.StateFlow
//import kotlinx.coroutines.flow.map
//import kotlinx.coroutines.flow.stateIn
//import kotlinx.coroutines.launch
//import javax.inject.Inject
//
//@HiltViewModel
//class MainViewModel @Inject constructor(
//    private val savedStateHandle: SavedStateHandle,
//    private val saveDefaultTaskUseCase: SaveDefaultTaskUseCase
//) : BaseViewModel<MainScreenEvent, MainScreenState, MainScreenNavigation, MainScreenConfig>() {
//
//    override val uiScreenState: StateFlow<MainScreenState> = MutableStateFlow(MainScreenState)
//
//    override fun onEvent(event: MainScreenEvent) {
//        when (event) {
//            is MainScreenEvent.OnCreateTaskClick -> onCreateTaskClick()
//            is MainScreenEvent.OnSearchTasksClick -> onSearchTasksClick()
//            is MainScreenEvent.ResultEvent -> onResultEvent(event)
//        }
//    }
//
//    private fun onSearchTasksClick() {
//        setUpNavigationState(MainScreenNavigation.SearchTasks)
//    }
//
//    private fun onResultEvent(event: MainScreenEvent.ResultEvent) {
//        when (event) {
//            is MainScreenEvent.ResultEvent.PickTaskType -> onPickTaskTypeResult(event)
//            is MainScreenEvent.ResultEvent.PickTaskProgressType ->
//                onPickTaskProgressTypeResult(event)
//        }
//    }
//
//    private fun onPickTaskProgressTypeResult(event: MainScreenEvent.ResultEvent.PickTaskProgressType) {
//        onIdleToAction {
//            when (val result = event.result) {
//                is PickTaskProgressTypeScreenResult.Confirm -> onConfirmPickTaskProgressType(result)
//                is PickTaskProgressTypeScreenResult.Dismiss -> Unit
//            }
//        }
//    }
//
//    private fun onConfirmPickTaskProgressType(result: PickTaskProgressTypeScreenResult.Confirm) {
//        onConfirmCreateTask(
//            taskType = TaskType.Habit,
//            taskProgressType = result.taskProgressType
//        )
//    }
//
//    private fun onPickTaskTypeResult(event: MainScreenEvent.ResultEvent.PickTaskType) {
//        onIdleToAction {
//            when (val result = event.result) {
//                is PickTaskTypeScreenResult.Confirm -> onConfirmPickTaskType(result)
//                is PickTaskTypeScreenResult.Dismiss -> Unit
//            }
//        }
//    }
//
//    private fun onConfirmPickTaskType(result: PickTaskTypeScreenResult.Confirm) {
//        onIdleToAction {
//            when (result.taskType) {
//                TaskType.SingleTask, TaskType.RecurringTask -> {
//                    onConfirmCreateTask(result.taskType, TaskProgressType.YesNo)
//                }
//
//                TaskType.Habit -> {
//                    setUpConfigState(
//                        MainScreenConfig.PickTaskProgressType(TaskProgressType.entries)
//                    )
//                }
//            }
//        }
//    }
//
//    private fun onConfirmCreateTask(taskType: TaskType, taskProgressType: TaskProgressType) {
//        onIdleToAction {
//            viewModelScope.launch {
//                when (val resultModel = saveDefaultTaskUseCase(taskType, taskProgressType)) {
//                    is ResultModel.Success -> {
//                        val taskId = resultModel.data
//                        setUpNavigationState(MainScreenNavigation.CreateTask(taskId))
//                    }
//
//                    is ResultModel.Error -> { /* TODO */
//                    }
//                }
//            }
//        }
//    }
//
//    private fun onCreateTaskClick() {
//        setUpConfigState(MainScreenConfig.PickTaskType(TaskType.entries))
//    }
//
//}