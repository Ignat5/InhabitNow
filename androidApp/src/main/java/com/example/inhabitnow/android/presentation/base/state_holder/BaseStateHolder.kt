package com.example.inhabitnow.android.presentation.base.state_holder

import com.example.inhabitnow.android.presentation.base.components.event.ScreenEvent
import com.example.inhabitnow.android.presentation.base.components.state.ScreenState
import kotlinx.coroutines.flow.StateFlow

abstract class BaseStateHolder<SE: ScreenEvent, SS: ScreenState> {

    abstract val uiScreenState: StateFlow<SS>
    abstract fun onEvent(event: SE)

}