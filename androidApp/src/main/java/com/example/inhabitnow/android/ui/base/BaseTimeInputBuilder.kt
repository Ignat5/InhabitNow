package com.example.inhabitnow.android.ui.base

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce

@OptIn(ExperimentalFoundationApi::class, FlowPreview::class)
object BaseTimeInputBuilder {
    private const val VISIBLE_ITEMS_COUNT = 3

    private val targetHeight = TextFieldDefaults.MinHeight * 2
    val itemHeight = targetHeight / VISIBLE_ITEMS_COUNT

    private const val DEFAULT_DEBOUNCE_MILLIS = 200L

    private const val HOURS_BASE = 24
    private const val HOURS_SEED = 89_478_479

    private const val MINUTES_BASE = 60
    private const val MINUTES_SEED = 35_791_379

    private val HOURS_RANGE = IntRange(0, HOURS_BASE - 1)
    private val MINUTES_RANGE = IntRange(0, MINUTES_BASE - 1)

    private enum class TimeInputType(
        val base: Int,
        val seed: Int,
        val range: IntRange
    ) {
        Hours(
            base = HOURS_BASE,
            seed = HOURS_SEED,
            range = HOURS_RANGE
        ),
        Minutes(
            base = MINUTES_BASE,
            seed = MINUTES_SEED,
            range = MINUTES_RANGE
        );
    }

    @Composable
    fun BaseTimeInput(
        initHours: Int,
        initMinutes: Int,
        onHoursChanged: (hours: Int) -> Unit,
        onMinutesChanged: (minutes: Int) -> Unit,
        modifier: Modifier = Modifier
    ) {
        val hours = remember { initHours }
        val minutes = remember { initMinutes }
        Box(modifier = modifier) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TimeInputLazyColumn(
                    timeInputType = TimeInputType.Hours,
                    initValue = hours,
                    onValueChanged = onHoursChanged,
                    modifier = Modifier.weight(1f)
                )
                Box(
                    modifier = Modifier
                        .height(itemHeight)
                        .align(Alignment.Top)
                ) {
                    HoursMinutesDivider(modifier = Modifier.align(Alignment.Center))
                }
                TimeInputLazyColumn(
                    timeInputType = TimeInputType.Minutes,
                    initValue = minutes,
                    onValueChanged = onMinutesChanged,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }

    @Composable
    private fun TimeInputLazyColumn(
        timeInputType: TimeInputType,
        initValue: Int,
        onValueChanged: (value: Int) -> Unit,
        modifier: Modifier = Modifier
    ) {
        BoxInputContainer(modifier = modifier) {
            val lazyListState = rememberLazyListState(initialFirstVisibleItemIndex = initValue)
            val flingBehavior = rememberSnapFlingBehavior(lazyListState = lazyListState)
            val rangeList = remember(timeInputType) {
                timeInputType.range.toList()
            }
            LazyColumn(
                state = lazyListState,
                flingBehavior = flingBehavior,
                modifier = Modifier
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.Center
            ) {
                items(rangeList) { item ->
                    val isSelected by remember {
                        derivedStateOf {
                            item == lazyListState.firstVisibleItemIndex
                        }
                    }
                    ItemTimeValue(
                        value = item,
                        isSelected = isSelected
                    )
                }
                items(VISIBLE_ITEMS_COUNT - 1) {
                    Box(modifier = Modifier.height(itemHeight))
                }
            }
            BoxCurrentFrame(modifier = Modifier.align(Alignment.TopCenter))
            LaunchedEffect(Unit) {
                snapshotFlow { lazyListState.firstVisibleItemIndex }
                    .debounce(DEFAULT_DEBOUNCE_MILLIS)
                    .collectLatest { index ->
                        onValueChanged(index)
                    }
            }
        }
    }

    @Composable
    fun BaseTimePicker(
        initHours: Int,
        initMinutes: Int,
        onHoursChanged: (hours: Int) -> Unit,
        onMinutesChanged: (minutes: Int) -> Unit,
        modifier: Modifier
    ) {
        val hours = remember { initHours }
        val minutes = remember { initMinutes }
        Box(modifier = modifier) {
            Row(
                modifier = Modifier,
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TimePickerLazyColumn(
                    timeInputType = TimeInputType.Hours,
                    initValue = hours,
                    onValueChanged = onHoursChanged,
                    modifier = Modifier.weight(1f)
                )
                HoursMinutesDivider()
                TimePickerLazyColumn(
                    timeInputType = TimeInputType.Minutes,
                    initValue = minutes,
                    onValueChanged = onMinutesChanged,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }

    @Composable
    private fun TimePickerLazyColumn(
        timeInputType: TimeInputType,
        initValue: Int,
        onValueChanged: (value: Int) -> Unit,
        modifier: Modifier = Modifier
    ) {
        BoxInputContainer(modifier = modifier) {
            val seed = remember { timeInputType.seed }
            val initIndex = remember { seed + initValue }
            val lazyListState = rememberLazyListState(initialFirstVisibleItemIndex = initIndex)
            val flingBehavior = rememberSnapFlingBehavior(lazyListState = lazyListState)
            val currentIndex by remember {
                derivedStateOf {
                    lazyListState.firstVisibleItemIndex + 1
                }
            }
            LazyColumn(
                state = lazyListState,
                flingBehavior = flingBehavior,
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Center
            ) {
                items(Int.MAX_VALUE) { item ->
                    val value = remember {
                        item % timeInputType.base
                    }
                    val isSelected = remember(currentIndex) {
                        item == currentIndex
                    }
                    ItemTimeValue(value = value, isSelected = isSelected)
                }
            }
            BoxCurrentFrame(modifier = Modifier.align(Alignment.Center))
            LaunchedEffect(Unit) {
                snapshotFlow { currentIndex }
                    .debounce(DEFAULT_DEBOUNCE_MILLIS)
                    .collectLatest { value ->
                        onValueChanged(value % timeInputType.base)
                    }
            }
        }
    }

    @Composable
    private fun HoursMinutesDivider(modifier: Modifier = Modifier) {
        Text(
            text = ":",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = modifier
        )
    }

    @Composable
    private fun BoxCurrentFrame(modifier: Modifier = Modifier) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .height(itemHeight)
                .border(
                    width = OutlinedTextFieldDefaults.UnfocusedBorderThickness,
                    color = MaterialTheme.colorScheme.outline,
                    shape = MaterialTheme.shapes.small
                )
        )
    }

    @Composable
    private fun BoxInputContainer(
        modifier: Modifier = Modifier,
        content: @Composable BoxScope.() -> Unit
    ) {
        Box(modifier = modifier.height(targetHeight)) {
            content()
        }
    }

    @Composable
    private fun ItemTimeValue(
        value: Int,
        isSelected: Boolean
    ) {
        val valueText = remember {
            if (value <= 9) "0$value" else "$value"
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(itemHeight),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = valueText,
                modifier = Modifier
                    .fillMaxWidth(),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleMedium,
                color = if (isSelected) MaterialTheme.colorScheme.onSurface
                else MaterialTheme.colorScheme.outline
            )
        }
    }

}