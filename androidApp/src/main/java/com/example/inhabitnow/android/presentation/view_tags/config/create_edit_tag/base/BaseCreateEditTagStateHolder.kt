package com.example.inhabitnow.android.presentation.view_tags.config.create_edit_tag.base

import com.example.inhabitnow.android.presentation.base.components.event.ScreenEvent
import com.example.inhabitnow.android.presentation.base.components.result.ScreenResult
import com.example.inhabitnow.android.presentation.base.state_holder.BaseResultStateHolder
import com.example.inhabitnow.android.presentation.view_tags.config.create_edit_tag.base.components.BaseCreateEditTagScreenEvent
import com.example.inhabitnow.android.presentation.view_tags.config.create_edit_tag.base.components.BaseCreateEditTagScreenState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

abstract class BaseCreateEditTagStateHolder<SE : ScreenEvent, SS : BaseCreateEditTagScreenState, SR : ScreenResult>(
    initTitle: String? = null,
    final override val holderScope: CoroutineScope
) : BaseResultStateHolder<SE, SS, SR>() {

    abstract fun onConfirm()
    abstract fun onDismiss()

    protected val inputTitleState = MutableStateFlow(initTitle ?: DEFAULT_TITLE)
    protected val canConfirmState = inputTitleState.map { inputTitle ->
        inputTitle.isNotBlank()
    }.stateIn(
        holderScope,
        SharingStarted.Eagerly,
        false
    )

    protected fun onBaseEvent(event: BaseCreateEditTagScreenEvent) {
        when (event) {
            is BaseCreateEditTagScreenEvent.OnInputUpdateTitle -> onInputUpdateTitle(event)
            is BaseCreateEditTagScreenEvent.OnConfirmClick -> onConfirmClick()
            is BaseCreateEditTagScreenEvent.OnDismissRequest -> onDismissRequest()
        }
    }

    private fun onInputUpdateTitle(event: BaseCreateEditTagScreenEvent.OnInputUpdateTitle) {
        inputTitleState.update { event.title }
    }

    private fun onConfirmClick() {
        if (canConfirmState.value) {
            onConfirm()
        }
    }

    private fun onDismissRequest() {
        onDismiss()
    }

    companion object {
        private const val DEFAULT_TITLE = ""
    }
}