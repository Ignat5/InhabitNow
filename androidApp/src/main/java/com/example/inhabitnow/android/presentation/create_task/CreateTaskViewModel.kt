package com.example.inhabitnow.android.presentation.create_task

import androidx.lifecycle.SavedStateHandle
import com.example.inhabitnow.android.presentation.base.view_model.BaseViewModel
import com.example.inhabitnow.android.presentation.create_task.components.CreateTaskScreenConfig
import com.example.inhabitnow.android.presentation.create_task.components.CreateTaskScreenEvent
import com.example.inhabitnow.android.presentation.create_task.components.CreateTaskScreenNavigation
import com.example.inhabitnow.android.presentation.create_task.components.CreateTaskScreenState
import com.example.inhabitnow.domain.use_case.read_task_with_content_by_id.ReadTaskWithContentByIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class CreateTaskViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val readTaskWithContentByIdUseCase: ReadTaskWithContentByIdUseCase
): BaseViewModel<CreateTaskScreenEvent, CreateTaskScreenState, CreateTaskScreenNavigation, CreateTaskScreenConfig>() {

    override val uiScreenState: StateFlow<CreateTaskScreenState>
        get() = TODO("Not yet implemented")

    override fun onEvent(event: CreateTaskScreenEvent) {
        TODO("Not yet implemented")
    }

}