package com.example.inhabitnow.android.ui.base

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun BaseTimePicker(
    initHours: Int,
    initMinutes: Int,
    onHoursChanged: (Int) -> Unit,
    onMinutesChanged: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        TimeUnitLazyColumn(
            timeUnitType = TimeUnitType.Hours,
            initValue = initHours,
            onValueChanged = onHoursChanged,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = ":",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
        TimeUnitLazyColumn(
            timeUnitType = TimeUnitType.Minutes,
            initValue = initMinutes,
            onValueChanged = onMinutesChanged,
            modifier = Modifier.weight(1f)
        )
    }
}

private val targetHeight = TextFieldDefaults.MinHeight * 2
private val itemHeight = targetHeight / 3

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun TimeUnitLazyColumn(
    timeUnitType: TimeUnitType,
    initValue: Int,
    onValueChanged: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .height(targetHeight)
    ) {
        val seed = remember { timeUnitType.seed }
        val initIndex = remember { seed + initValue }
        val lazyListState = rememberLazyListState(initialFirstVisibleItemIndex = initIndex)
        val flingBehavior = rememberSnapFlingBehavior(lazyListState = lazyListState)
        var currentItemIndex by remember {
            mutableIntStateOf(lazyListState.firstVisibleItemIndex + 1)
        }
        LazyColumn(
            state = lazyListState,
            flingBehavior = flingBehavior,
            modifier = Modifier
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Center
        ) {
            items(Int.MAX_VALUE) { item ->
                val value = remember {
                    item % timeUnitType.base
                }
                val isSelected = remember(currentItemIndex) {
                    item == currentItemIndex
                }
                ItemTimeValue(
                    value = value,
                    isSelected = isSelected
                )
            }
        }

        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth()
                .height(itemHeight)
                .border(
                    width = OutlinedTextFieldDefaults.UnfocusedBorderThickness,
                    color = MaterialTheme.colorScheme.outline,
                    shape = MaterialTheme.shapes.small
                )
        )

        LaunchedEffect(lazyListState.isScrollInProgress) {
            if (!lazyListState.isScrollInProgress) {
                currentItemIndex = lazyListState.firstVisibleItemIndex + 1
            }
        }

        LaunchedEffect(currentItemIndex) {
            onValueChanged(currentItemIndex % timeUnitType.base)
        }
    }
}

@Composable
fun ItemTimeValue(
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
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleMedium,
            color = if (isSelected) MaterialTheme.colorScheme.onSurface
            else MaterialTheme.colorScheme.outline
        )
    }
}

private const val HOURS_BASE = 24
private const val HOURS_SEED = 89_478_479

private const val MINUTES_BASE = 60
private const val MINUTES_SEED = 35_791_379

private enum class TimeUnitType(
    val base: Int,
    val seed: Int
) {
    Hours(
        base = HOURS_BASE,
        seed = HOURS_SEED
    ),
    Minutes(
        base = MINUTES_BASE,
        seed = MINUTES_SEED
    );
}