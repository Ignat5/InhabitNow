package com.example.inhabitnow.android.presentation.main

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.inhabitnow.android.presentation.base.components.config.BaseConfigState
import com.example.inhabitnow.android.presentation.base.view_model.BaseViewModel
import com.example.inhabitnow.android.presentation.main.components.MainScreenConfig
import com.example.inhabitnow.android.presentation.main.components.MainScreenEvent
import com.example.inhabitnow.android.presentation.main.components.MainScreenNavigation
import com.example.inhabitnow.android.presentation.main.components.MainScreenState
import com.example.inhabitnow.core.type.TaskType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : BaseViewModel<MainScreenEvent, MainScreenState, MainScreenNavigation, MainScreenConfig>() {

    override val uiScreenState: StateFlow<MainScreenState> = MutableStateFlow(MainScreenState)

    override fun onEvent(event: MainScreenEvent) {
        when (event) {
            is MainScreenEvent.OnCreateTaskClick -> onCreateTaskClick()
        }
    }

    private fun onCreateTaskClick() {
        setUpConfigState(MainScreenConfig.PickTaskType(TaskType.entries))
    }

}