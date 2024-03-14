package com.example.inhabitnow.android.presentation.base.state_holder

import com.example.inhabitnow.android.presentation.base.components.event.ScreenEvent
import com.example.inhabitnow.android.presentation.base.components.result.BaseResultState
import com.example.inhabitnow.android.presentation.base.components.result.ScreenResult
import com.example.inhabitnow.android.presentation.base.components.state.ScreenState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

abstract class BaseResultStateHolder<SE : ScreenEvent, SS : ScreenState, SR : ScreenResult> : BaseStateHolder<SE, SS>() {
    private val _uiScreenResult = MutableStateFlow<BaseResultState<SR>>(BaseResultState.Idle)
    val uiScreenResult: StateFlow<BaseResultState<SR>> = _uiScreenResult

    protected fun setUpResult(result: SR) = _uiScreenResult.update {
        BaseResultState.Result(result)
    }

    fun onResultHandled() = _uiScreenResult.update { BaseResultState.Idle }
}