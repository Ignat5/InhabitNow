package com.example.inhabitnow.android.presentation.view_tags.config.create_edit_tag.edit

import androidx.compose.runtime.Composable
import com.example.inhabitnow.android.presentation.base.ext.BaseScreen
import com.example.inhabitnow.android.presentation.view_tags.config.create_edit_tag.base.BaseCreateEditTagDialog
import com.example.inhabitnow.android.presentation.view_tags.config.create_edit_tag.edit.components.EditTagScreenEvent
import com.example.inhabitnow.android.presentation.view_tags.config.create_edit_tag.edit.components.EditTagScreenResult
import com.example.inhabitnow.android.presentation.view_tags.config.create_edit_tag.edit.components.EditTagScreenState

@Composable
fun EditTagDialog(
    stateHolder: EditTagStateHolder,
    onResult: (EditTagScreenResult) -> Unit
) {
    BaseScreen(stateHolder = stateHolder, onResult = onResult) { state, onEvent ->
        EditTagDialogStateless(state, onEvent)
    }
}

@Composable
private fun EditTagDialogStateless(
    state: EditTagScreenState,
    onEvent: (EditTagScreenEvent) -> Unit
) {
    BaseCreateEditTagDialog(
        isCreate = false,
        state = state,
        onEvent = { onEvent(EditTagScreenEvent.BaseEvent(it)) }
    )
}