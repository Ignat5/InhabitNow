package com.example.inhabitnow.android.presentation.create_edit_task.create

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.inhabitnow.android.R
import com.example.inhabitnow.android.presentation.base.ext.BaseScreen
import com.example.inhabitnow.android.presentation.create_edit_task.base.BaseCreateEditTaskBuilder
import com.example.inhabitnow.android.presentation.create_edit_task.base.components.BaseCreateEditTaskScreenEvent
import com.example.inhabitnow.android.presentation.create_edit_task.common.config.confirm_leave.ConfirmLeaveDialog
import com.example.inhabitnow.android.presentation.create_edit_task.create.components.CreateTaskScreenConfig
import com.example.inhabitnow.android.presentation.create_edit_task.create.components.CreateTaskScreenEvent
import com.example.inhabitnow.android.presentation.create_edit_task.create.components.CreateTaskScreenNavigation
import com.example.inhabitnow.android.presentation.create_edit_task.create.components.CreateTaskScreenState

@Composable
fun CreateTaskScreen(onNavigation: (CreateTaskScreenNavigation) -> Unit) {
    val viewModel: CreateTaskViewModel = hiltViewModel()
    BaseScreen(
        viewModel = viewModel,
        onNavigation = onNavigation,
        configContent = { config, onEvent ->
            CreateTaskScreenConfigStateless(
                config = config,
                onResultEvent = { resultEvent ->
                    onEvent(resultEvent)
                }
            )
        }
    ) { state, onEvent ->
        CreateTaskScreenStateless(state, onEvent)
    }
}

@Composable
private fun CreateTaskScreenStateless(
    state: CreateTaskScreenState,
    onEvent: (CreateTaskScreenEvent) -> Unit
) {
    BackHandler { onEvent(CreateTaskScreenEvent.OnDismissRequest) }
    Scaffold(
        topBar = {
            ScreenTopBar(
                canSave = state.canSave,
                onSaveClick = { onEvent(CreateTaskScreenEvent.OnSaveClick) },
                onCloseClick = { onEvent(CreateTaskScreenEvent.OnDismissRequest) }
            )
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                BaseCreateEditTaskBuilder.baseConfigItems(
                    lazyListScope = this,
                    allTaskConfigItems = state.allTaskConfigItems,
                    onItemClick = { item ->
                        onEvent(
                            CreateTaskScreenEvent.Base(
                                BaseCreateEditTaskScreenEvent.OnBaseItemTaskConfigClick(
                                    item
                                )
                            )
                        )
                    }
                )
            }
        }
    }
}

@Composable
private fun CreateTaskScreenConfigStateless(
    config: CreateTaskScreenConfig,
    onResultEvent: (CreateTaskScreenEvent) -> Unit
) {
    when (config) {
        is CreateTaskScreenConfig.Base -> {
            BaseCreateEditTaskBuilder.BaseScreenConfig(
                baseConfig = config.baseConfig,
                onBaseResultEvent = {
                    onResultEvent(CreateTaskScreenEvent.Base(it))
                }
            )
        }

        is CreateTaskScreenConfig.ConfirmLeave -> {
            ConfirmLeaveDialog(
                onResult = {
                    onResultEvent(CreateTaskScreenEvent.ResultEvent.ConfirmLeave(it))
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ScreenTopBar(
    canSave: Boolean,
    onSaveClick: () -> Unit,
    onCloseClick: () -> Unit
) {
    TopAppBar(
        title = {
            Text(text = "Create activity")
        },
        navigationIcon = {
            IconButton(onClick = onCloseClick) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_close),
                    tint = MaterialTheme.colorScheme.onSurface,
                    contentDescription = null
                )
            }
        },
        actions = {
            TextButton(
                onClick = onSaveClick,
                enabled = canSave
            ) {
                Text(text = "save")
            }
        }
    )
}