package com.example.inhabitnow.android.presentation.view_tags.config.create_edit_tag.create

import androidx.compose.runtime.Composable
import com.example.inhabitnow.android.presentation.base.ext.BaseScreen
import com.example.inhabitnow.android.presentation.view_tags.config.create_edit_tag.base.BaseCreateEditTagDialog
import com.example.inhabitnow.android.presentation.view_tags.config.create_edit_tag.create.components.CreateTagScreenEvent
import com.example.inhabitnow.android.presentation.view_tags.config.create_edit_tag.create.components.CreateTagScreenResult
import com.example.inhabitnow.android.presentation.view_tags.config.create_edit_tag.create.components.CreateTagScreenState

@Composable
fun CreateTagDialog(
    stateHolder: CreateTagStateHolder,
    onResult: (CreateTagScreenResult) -> Unit
) {
    BaseScreen(stateHolder = stateHolder, onResult = onResult) { state, onEvent ->
        CreateTagDialogStateless(state, onEvent)
    }
}

@Composable
private fun CreateTagDialogStateless(
    state: CreateTagScreenState,
    onEvent: (CreateTagScreenEvent) -> Unit
) {
    BaseCreateEditTagDialog(
        isCreate = true,
        state = state,
        onEvent = { onEvent(CreateTagScreenEvent.BaseEvent(it)) }
    )
}