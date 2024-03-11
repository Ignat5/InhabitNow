package com.example.inhabitnow.android.presentation.base.view_model

import androidx.lifecycle.ViewModel
import com.example.inhabitnow.android.presentation.base.components.config.BaseConfigState
import com.example.inhabitnow.android.presentation.base.components.config.ScreenConfig
import com.example.inhabitnow.android.presentation.base.components.navigation.BaseNavigationState
import com.example.inhabitnow.android.presentation.base.components.navigation.ScreenNavigation
import com.example.inhabitnow.android.presentation.base.components.event.ScreenEvent
import com.example.inhabitnow.android.presentation.base.components.state.ScreenState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

abstract class BaseViewModel<SE : ScreenEvent, SS : ScreenState, SN : ScreenNavigation, SC : ScreenConfig> :
    ViewModel() {

    /* base */
    abstract val uiScreenState: StateFlow<SS>
    abstract fun onEvent(event: SE)


    /* navigation */
    private val _uiScreenNavigationState =
        MutableStateFlow<BaseNavigationState<SN>>(BaseNavigationState.Idle)

    val uiScreenNavigationState: StateFlow<BaseNavigationState<SN>> = _uiScreenNavigationState

    protected fun navigate(destination: SN) = _uiScreenNavigationState.update {
        BaseNavigationState.Destination(destination)
    }

    fun onNavigationHandled() = _uiScreenNavigationState.update { BaseNavigationState.Idle }


    /* config */
    private val _uiScreenConfigState = MutableStateFlow<BaseConfigState<SC>>(BaseConfigState.Idle)

    val uiScreenConfigState: StateFlow<BaseConfigState<SC>> = _uiScreenConfigState

    protected fun setUpConfigState(config: SC) = _uiScreenConfigState.update {
        BaseConfigState.Config(config)
    }

    protected inline fun onIdleToAction(action: () -> Unit) {
        resetConfigState()
        action()
    }

    protected inline fun onActionToIdle(action: () -> Unit) {
        action()
        resetConfigState()
    }

    protected fun resetConfigState() = _uiScreenConfigState.update { BaseConfigState.Idle }

}