package com.example.inhabitnow.android.presentation.view_tags.config.create_edit_tag.edit

import com.example.inhabitnow.android.presentation.view_tags.config.create_edit_tag.base.BaseCreateEditTagStateHolder
import com.example.inhabitnow.android.presentation.view_tags.config.create_edit_tag.edit.components.EditTagScreenEvent
import com.example.inhabitnow.android.presentation.view_tags.config.create_edit_tag.edit.components.EditTagScreenResult
import com.example.inhabitnow.android.presentation.view_tags.config.create_edit_tag.edit.components.EditTagScreenState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class EditTagStateHolder(
    private val tagId: String,
    tagTitle: String,
    holderScope: CoroutineScope
) : BaseCreateEditTagStateHolder<EditTagScreenEvent, EditTagScreenState, EditTagScreenResult>(
    initTitle = tagTitle,
    holderScope = holderScope
) {

    override val uiScreenState: StateFlow<EditTagScreenState> =
        combine(inputTitleState, canConfirmState) { inputTitle, canConfirm ->
            EditTagScreenState(
                inputTitle = inputTitle,
                canConfirm = canConfirm
            )
        }.stateIn(
            holderScope,
            SharingStarted.WhileSubscribed(5000L),
            EditTagScreenState(
                inputTitle = inputTitleState.value,
                canConfirm = canConfirmState.value
            )
        )

    override fun onEvent(event: EditTagScreenEvent) {
        when (event) {
            is EditTagScreenEvent.BaseEvent -> onBaseEvent(event.baseEvent)
        }
    }

    override fun onConfirm() {
        setUpResult(
            EditTagScreenResult.Confirm(
                tagId = tagId,
                tagTitle = inputTitleState.value
            )
        )
    }

    override fun onDismiss() {
        setUpResult(EditTagScreenResult.Dismiss)
    }
}