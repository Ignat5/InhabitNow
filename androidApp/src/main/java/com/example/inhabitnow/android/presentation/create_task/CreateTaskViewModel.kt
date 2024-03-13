package com.example.inhabitnow.android.presentation.create_task

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.inhabitnow.android.navigation.AppNavDest
import com.example.inhabitnow.android.presentation.base.view_model.BaseViewModel
import com.example.inhabitnow.android.presentation.create_task.components.CreateTaskScreenConfig
import com.example.inhabitnow.android.presentation.create_task.components.CreateTaskScreenEvent
import com.example.inhabitnow.android.presentation.create_task.components.CreateTaskScreenNavigation
import com.example.inhabitnow.android.presentation.create_task.components.CreateTaskScreenState
import com.example.inhabitnow.domain.use_case.read_task_with_content_by_id.ReadTaskWithContentByIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class CreateTaskViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val readTaskWithContentByIdUseCase: ReadTaskWithContentByIdUseCase
) : BaseViewModel<CreateTaskScreenEvent, CreateTaskScreenState, CreateTaskScreenNavigation, CreateTaskScreenConfig>() {

    private val taskId: String = checkNotNull(savedStateHandle.get<String>(AppNavDest.TASK_ID_KEY))

    private val taskWithContentState = readTaskWithContentByIdUseCase(taskId)
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            null
        )

    override val uiScreenState: StateFlow<CreateTaskScreenState> =
        taskWithContentState.map { taskWithContent ->
            CreateTaskScreenState(
                taskWithContent = taskWithContent,
                canSave = taskWithContent?.task?.title?.isNotBlank() == true
            )
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            CreateTaskScreenState(
                taskWithContent = taskWithContentState.value,
                canSave = false
            )
        )

    override fun onEvent(event: CreateTaskScreenEvent) {

    }

}