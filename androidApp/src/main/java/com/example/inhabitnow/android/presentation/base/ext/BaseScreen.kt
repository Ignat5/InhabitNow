package com.example.inhabitnow.android.presentation.base.ext

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.inhabitnow.android.presentation.base.components.event.ScreenEvent
import com.example.inhabitnow.android.presentation.base.components.navigation.BaseNavigationState
import com.example.inhabitnow.android.presentation.base.components.navigation.ScreenNavigation
import com.example.inhabitnow.android.presentation.base.components.result.BaseResultState
import com.example.inhabitnow.android.presentation.base.components.result.ScreenResult
import com.example.inhabitnow.android.presentation.base.components.state.ScreenState
import com.example.inhabitnow.android.presentation.base.state_holder.BaseResultStateHolder
import com.example.inhabitnow.android.presentation.base.view_model.BaseViewModel

@Composable
inline fun <SE : ScreenEvent, SS : ScreenState, SN: ScreenNavigation> BaseScreen(
    viewModel: BaseViewModel<SE, SS, SN>,
    crossinline onNavigation: (destination: SN) -> Unit,
    crossinline content: @Composable (state: SS, onEvent: (SE) -> Unit) -> Unit
) {
    val state by viewModel.uiScreenState.collectAsStateWithLifecycle()
    val baseNavigationState by viewModel.uiNavigationState.collectAsStateWithLifecycle()
    val onEvent = remember {
        val callback: (event: SE) -> Unit = { event ->
            viewModel.onEvent(event)
        }
        callback
    }
    content(state, onEvent)
    LaunchedEffect(baseNavigationState) {
        when (val baseNS = baseNavigationState) {
            is BaseNavigationState.Idle -> Unit
            is BaseNavigationState.Destination -> {
                onNavigation(baseNS.destination)
                viewModel.onNavigationHandled()
            }
        }
    }
}

@Composable
inline fun <SE : ScreenEvent, SS : ScreenState, SR : ScreenResult> BaseScreen(
    stateHolder: BaseResultStateHolder<SE, SS, SR>,
    crossinline onResult: (result: SR) -> Unit,
    crossinline content: @Composable (state: SS, onEvent: (SE) -> Unit) -> Unit
) {
    val state by stateHolder.uiScreenState.collectAsStateWithLifecycle()
    val baseResultState by stateHolder.uiScreenResult.collectAsStateWithLifecycle()
    val onEvent = remember {
        val callback: (event: SE) -> Unit = { event ->
            stateHolder.onEvent(event)
        }
        callback
    }
    content(state, onEvent)
    LaunchedEffect(baseResultState) {
        when (val baseRS = baseResultState) {
            is BaseResultState.Idle -> Unit
            is BaseResultState.Result -> {
                onResult(baseRS.result)
                stateHolder.onResultHandled()
            }
        }
    }
}