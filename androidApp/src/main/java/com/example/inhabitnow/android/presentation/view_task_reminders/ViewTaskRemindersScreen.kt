package com.example.inhabitnow.android.presentation.view_task_reminders

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.inhabitnow.android.R
import com.example.inhabitnow.android.presentation.base.ext.BaseScreen
import com.example.inhabitnow.android.presentation.model.UIResultModel
import com.example.inhabitnow.android.presentation.view_task_reminders.components.ViewTaskRemindersScreenConfig
import com.example.inhabitnow.android.presentation.view_task_reminders.components.ViewTaskRemindersScreenEvent
import com.example.inhabitnow.android.presentation.view_task_reminders.components.ViewTaskRemindersScreenNavigation
import com.example.inhabitnow.android.presentation.view_task_reminders.components.ViewTaskRemindersScreenState
import com.example.inhabitnow.android.presentation.view_task_reminders.config.create_reminder.CreateReminderDialog
import com.example.inhabitnow.android.ui.toDisplay
import com.example.inhabitnow.android.ui.toHourMinute
import com.example.inhabitnow.android.ui.toIconResId
import com.example.inhabitnow.domain.model.reminder.ReminderModel

@Composable
fun ViewTaskRemindersScreen(
    onNavigate: (ViewTaskRemindersScreenNavigation) -> Unit
) {
    val viewModel: ViewTaskRemindersViewModel = hiltViewModel()
    BaseScreen(
        viewModel = viewModel,
        onNavigation = onNavigate,
        configContent = { config ->
            ScreenConfigStateless(
                config = config,
                onResult = { result ->
                    viewModel.onEvent(result)
                }
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
                                    .padding(8.dp)
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
        }
    }
}

@Composable
private fun ScreenConfigStateless(
    config: ViewTaskRemindersScreenConfig,
    onResult: (ViewTaskRemindersScreenEvent.ResultEvent) -> Unit
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
    OutlinedButton(
        onClick = onClick,
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
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
                text = "Add",
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

