package com.example.inhabitnow.android.presentation.view_task_reminders

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.BackHandler
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.inhabitnow.android.R
import com.example.inhabitnow.android.presentation.base.ext.BaseScreen
import com.example.inhabitnow.android.presentation.model.UIResultModel
import com.example.inhabitnow.android.presentation.view_task_reminders.components.ViewTaskRemindersScreenConfig
import com.example.inhabitnow.android.presentation.view_task_reminders.components.ViewTaskRemindersScreenEvent
import com.example.inhabitnow.android.presentation.view_task_reminders.components.ViewTaskRemindersScreenNavigation
import com.example.inhabitnow.android.presentation.view_task_reminders.components.ViewTaskRemindersScreenState
import com.example.inhabitnow.android.presentation.view_task_reminders.config.confirm_delete_reminder.ConfirmDeleteReminderDialog
import com.example.inhabitnow.android.presentation.view_task_reminders.config.create_edit_reminder.create.CreateReminderDialog
import com.example.inhabitnow.android.presentation.view_task_reminders.config.create_edit_reminder.edit.EditReminderDialog
import com.example.inhabitnow.android.presentation.view_task_reminders.config.permission.CheckNotificationPermissionScreenResult
import com.example.inhabitnow.android.presentation.view_task_reminders.config.permission.NotificationPermissionRationaleDialog
import com.example.inhabitnow.android.ui.currentActivity
import com.example.inhabitnow.android.ui.toDisplay
import com.example.inhabitnow.android.ui.toHourMinute
import com.example.inhabitnow.android.ui.toIconResId
import com.example.inhabitnow.domain.model.reminder.ReminderModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberPermissionState
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged

@Composable
fun ViewTaskRemindersScreen(
    onNavigate: (ViewTaskRemindersScreenNavigation) -> Unit
) {
    val viewModel: ViewTaskRemindersViewModel = hiltViewModel()
    BaseScreen(
        viewModel = viewModel,
        onNavigation = onNavigate,
        configContent = { config, onEvent ->
            ScreenConfigStateless(
                config = config,
                onResult = { result -> onEvent(result) }
            )
        }
    ) { state, onEvent ->
        ViewTaskRemindersScreenStateless(state, onEvent)
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ViewTaskRemindersScreenStateless(
    state: ViewTaskRemindersScreenState,
    onEvent: (ViewTaskRemindersScreenEvent) -> Unit
) {
    BackHandler { onEvent(ViewTaskRemindersScreenEvent.OnBackClick) }
    Scaffold(
        topBar = {
            ScreenTopBar(onBackClick = { onEvent(ViewTaskRemindersScreenEvent.OnBackClick) })
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxWidth()
            ) {
                when (state.allRemindersResultModel) {
                    is UIResultModel.Loading, is UIResultModel.Data -> {
                        items(
                            items = state.allRemindersResultModel.data ?: emptyList(),
                            key = { item -> item.id },
                            contentType = { ScreenItemKey.Reminder.name }
                        ) { item ->
                            ItemReminder(
                                item = item,
                                onClick = {
                                    onEvent(ViewTaskRemindersScreenEvent.OnReminderClick(item.id))
                                },
                                onDeleteClick = {
                                    onEvent(ViewTaskRemindersScreenEvent.OnDeleteReminderClick(item.id))
                                },
                                modifier = Modifier.animateItemPlacement()
                            )
                        }
                    }

                    is UIResultModel.NoData -> {
                        item(
                            key = ScreenItemKey.NoRemindersMessage.ordinal,
                            contentType = ScreenItemKey.NoRemindersMessage.ordinal
                        ) {
                            NoRemindersMessage(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 8.dp)
                            )
                        }
                    }
                }

                item(
                    key = ScreenItemKey.CreateReminderButton.ordinal,
                    contentType = ScreenItemKey.CreateReminderButton.ordinal
                ) {
                    CreateReminderButton(
                        onClick = {
                            onEvent(ViewTaskRemindersScreenEvent.OnCreateReminderClick)
                        },
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }

//            if (state.shouldCheckNotificationPermission) {
//                PermissionHandler(onResult = { result ->
//                    onEvent(
//                        ViewTaskRemindersScreenEvent.ResultEvent.CheckNotificationPermission(
//                            result
//                        )
//                    )
//                })
//            }
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun PermissionHandler(
    shouldSkipRationale: Boolean,
    onResult: (CheckNotificationPermissionScreenResult) -> Unit
) {
    val context = LocalContext.current
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        val permissionState =
            rememberPermissionState(permission = Manifest.permission.POST_NOTIFICATIONS)
        LaunchedEffect(Unit) {
            snapshotFlow { permissionState.status }
                .distinctUntilChanged()
                .collectLatest { status ->
                    when (status) {
                        is PermissionStatus.Granted -> {
                            onResult(CheckNotificationPermissionScreenResult.Granted)
                        }

                        is PermissionStatus.Denied -> {
                            if (shouldSkipRationale) {
                                permissionState.launchPermissionRequest()
                            } else {
                                if (status.shouldShowRationale) {
                                    onResult(CheckNotificationPermissionScreenResult.ShowRationale)
                                } else {
                                    permissionState.launchPermissionRequest()
                                }
                            }
                        }
                    }
                }
        }
    } else {
        if (NotificationManagerCompat.from(context).areNotificationsEnabled()) {
            onResult(CheckNotificationPermissionScreenResult.Granted)
        } else {
            onResult(CheckNotificationPermissionScreenResult.Denied)
        }
    }
}

@Composable
private fun ScreenConfigStateless(
    config: ViewTaskRemindersScreenConfig,
    onResult: (ViewTaskRemindersScreenEvent) -> Unit
) {
    when (config) {
        is ViewTaskRemindersScreenConfig.CreateReminder -> {
            CreateReminderDialog(
                stateHolder = config.stateHolder,
                onResult = {
                    onResult(
                        ViewTaskRemindersScreenEvent.ResultEvent.CreateReminder(it)
                    )
                }
            )
        }

        is ViewTaskRemindersScreenConfig.EditReminder -> {
            EditReminderDialog(
                stateHolder = config.stateHolder,
                onResult = {
                    onResult(ViewTaskRemindersScreenEvent.ResultEvent.EditReminder(it))
                }
            )
        }

        is ViewTaskRemindersScreenConfig.ConfirmDeleteReminder -> {
            ConfirmDeleteReminderDialog(
                reminderId = config.reminderId,
                onResult = {
                    onResult(ViewTaskRemindersScreenEvent.ResultEvent.ConfirmDeleteReminder(it))
                }
            )
        }

        is ViewTaskRemindersScreenConfig.CheckNotificationPermission -> {
            PermissionHandler(shouldSkipRationale = config.shouldSkipRationale) {
                onResult(ViewTaskRemindersScreenEvent.ResultEvent.CheckNotificationPermission(it))
            }
        }

        is ViewTaskRemindersScreenConfig.NotificationPermissionRationale -> {
            NotificationPermissionRationaleDialog(
                onDismissRequest = {
                    onResult(ViewTaskRemindersScreenEvent.OnNotificationPermissionRationalDismissRequest)
                }
            )
        }
    }
}

@Composable
private fun ItemReminder(
    item: ReminderModel,
    onClick: () -> Unit,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp, horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                painter = painterResource(item.type.toIconResId()),
                tint = MaterialTheme.colorScheme.onSurface,
                contentDescription = null
            )
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = item.time.toHourMinute(),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = item.schedule.toDisplay(),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            IconButton(onClick = onDeleteClick) {
                Icon(
                    painter = painterResource(R.drawable.ic_delete),
                    tint = MaterialTheme.colorScheme.onSurface,
                    contentDescription = null
                )
            }
        }
    }
}

@Composable
private fun CreateReminderButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    TextButton(
        onClick = onClick,
        modifier = modifier
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_add),
                tint = MaterialTheme.colorScheme.primary,
                contentDescription = null
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "New reminder",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
private fun NoRemindersMessage(modifier: Modifier = Modifier) {
    Text(
        text = "No reminders for this activity",
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        textAlign = TextAlign.Center,
        modifier = modifier
            .fillMaxWidth()
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ScreenTopBar(
    onBackClick: () -> Unit
) {
    TopAppBar(
        title = {
            Text(text = "Reminders")
        },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(painter = painterResource(id = R.drawable.ic_back), contentDescription = null)
            }
        }
    )
}

private enum class ScreenItemKey {
    NoRemindersMessage,
    Reminder,
    CreateReminderButton
}

