package com.example.inhabitnow.android.presentation.view_statistics

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.inhabitnow.android.R
import com.example.inhabitnow.android.presentation.base.ext.BaseScreen
import com.example.inhabitnow.android.presentation.view_statistics.components.ViewStatisticsScreenEvent
import com.example.inhabitnow.android.presentation.view_statistics.components.ViewStatisticsScreenNavigation
import com.example.inhabitnow.android.presentation.view_statistics.components.ViewStatisticsScreenState
import com.example.inhabitnow.android.presentation.view_statistics.model.UICompletionModel
import com.example.inhabitnow.android.presentation.view_statistics.model.UIStatisticsModel
import com.example.inhabitnow.android.presentation.view_statistics.model.UIStreakModel

@Composable
fun ViewStatisticsScreen(
    onNavigation: (ViewStatisticsScreenNavigation) -> Unit
) {
    val viewModel: ViewStatisticsViewModel = hiltViewModel()
    BaseScreen(
        viewModel = viewModel,
        onNavigation = onNavigation,
        configContent = { config, onEvent -> }
    ) { state, onEvent ->
        ViewStatisticsScreenStateless(state, onEvent)
    }
}

@Composable
private fun ViewStatisticsScreenStateless(
    state: ViewStatisticsScreenState,
    onEvent: (ViewStatisticsScreenEvent) -> Unit
) {
    Scaffold(
        topBar = {
            ScreenTopBar(
                taskTitle = state.habitModel?.title ?: "",
                onBackClick = { onEvent(ViewStatisticsScreenEvent.OnDismissRequest) }
            )
        }
    ) {
        BackHandler { onEvent(ViewStatisticsScreenEvent.OnDismissRequest) }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            val scrollState = rememberScrollState()
            Column(
                modifier = Modifier
                    .verticalScroll(scrollState)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(32.dp),
            ) {
                HabitScoreSection(state.uiStatisticsModel.habitScorePercent)
                StreakSection(state.uiStatisticsModel.streakModel)
                CompletionSection(state.uiStatisticsModel.completionModel)
            }
        }
    }
}

@Composable
private fun HabitScoreSection(habitScorePercent: Int) {
    Column(modifier = Modifier.fillMaxWidth()) {
        val ratio = remember(habitScorePercent) {
            (habitScorePercent.toFloat() / 100).toFloat()
        }
        val progress by animateFloatAsState(
            targetValue = ratio,
            animationSpec = spring<Float>(stiffness = Spring.StiffnessVeryLow),
            label = ""
        )
        SectionTitle(title = "Habit score")
        Spacer(modifier = Modifier.height(8.dp))
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                progress = { progress },
                modifier = Modifier.size(100.dp),
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.primaryContainer,
                strokeCap = ProgressIndicatorDefaults.CircularDeterminateStrokeCap,
                strokeWidth = 8.dp
            )
            Text(
                text = "$habitScorePercent",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

private enum class StreakType {
    Current,
    Best
}

@Composable
private fun StreakSection(
    streakModel: UIStreakModel
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        SectionTitle(title = "Streak")
        Spacer(modifier = Modifier.height(8.dp))
        val allStreakTypes = remember { StreakType.entries }
        allStreakTypes.forEachIndexed { index, streakType ->
            val title = remember {
                when (streakType) {
                    StreakType.Current -> "Current streak"
                    StreakType.Best -> "Best streak"
                }
            }
            val data = remember(streakModel) {
                when (streakType) {
                    StreakType.Current -> streakModel.currentStreak
                    StreakType.Best -> streakModel.bestStreak
                }
            }
            ItemStatistics(
                title = title,
                data = "$data"
            )
            if (index != allStreakTypes.lastIndex) {
                HorizontalDivider()
            }
        }
    }
}

private enum class CompletionType {
    Week,
    Month,
    Year,
    All
}

@Composable
private fun CompletionSection(completionModel: UICompletionModel) {
    Column(modifier = Modifier.fillMaxWidth()) {
        SectionTitle(title = "Times completed")
        Spacer(modifier = Modifier.height(8.dp))
        val allCompletionTypes = remember { CompletionType.entries }
        allCompletionTypes.forEachIndexed { index, completionType ->
            val title = remember {
                when (completionType) {
                    CompletionType.Week -> "This week"
                    CompletionType.Month -> "This month"
                    CompletionType.Year -> "This year"
                    CompletionType.All -> "All time"
                }
            }
            val data = remember(completionModel) {
                when (completionType) {
                    CompletionType.Week -> completionModel.currentWeekCompletionCount
                    CompletionType.Month -> completionModel.currentMonthCompletionCount
                    CompletionType.Year -> completionModel.currentYearCompletionCount
                    CompletionType.All -> completionModel.allTimeCompletionCount
                }
            }
            ItemStatistics(title = title, data = "$data")
            if (index != allCompletionTypes.lastIndex) {
                HorizontalDivider()
            }
        }
    }
}

@Composable
private fun ItemStatistics(
    title: String,
    data: String
) {
    Box(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = data,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1
            )
        }
    }
}

@Composable
private fun SectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.onSurface,
        modifier = Modifier.fillMaxWidth(),
        textAlign = TextAlign.Center
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ScreenTopBar(
    taskTitle: String,
    onBackClick: () -> Unit
) {
    TopAppBar(
        title = {
            Text(
                text = taskTitle,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(painter = painterResource(id = R.drawable.ic_back), contentDescription = null)
            }
        }
    )
}