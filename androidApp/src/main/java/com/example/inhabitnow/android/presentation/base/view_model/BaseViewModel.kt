package com.example.inhabitnow.android.presentation.base.view_model

import androidx.lifecycle.ViewModel
import com.example.inhabitnow.android.presentation.base.components.navigation.BaseNavigationState
import com.example.inhabitnow.android.presentation.base.components.navigation.ScreenNavigation
import com.example.inhabitnow.android.presentation.base.components.event.ScreenEvent
import com.example.inhabitnow.android.presentation.base.components.state.ScreenState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

abstract class BaseViewModel<SE : ScreenEvent, SS : ScreenState, SN : ScreenNavigation> :
    ViewModel() {

    abstract val uiScreenState: StateFlow<SS>
    abstract fun onEvent(event: SE)

    private val _uiNavigationState =
        MutableStateFlow<BaseNavigationState<SN>>(BaseNavigationState.Idle)

    val uiNavigationState: StateFlow<BaseNavigationState<SN>> = _uiNavigationState

    protected fun navigate(destination: SN) = _uiNavigationState.update {
        BaseNavigationState.Destination(destination)
    }

    fun onNavigationHandled() = _uiNavigationState.update { BaseNavigationState.Idle }

}