package com.example.inhabitnow.android.ui.base

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.example.inhabitnow.android.ui.toDisplay
import kotlinx.datetime.DayOfWeek

@Composable
fun BaseDaysOfWeekInput(
    selectedDaysOfWeek: Collection<DayOfWeek>,
    onDayOfWeekClick: (DayOfWeek) -> Unit,
    modifier: Modifier = Modifier
) {
    val allDaysOfWeek = remember { DayOfWeek.entries }

    LazyVerticalGrid(
        columns = GridCells.Fixed(allDaysOfWeek.size),
        modifier = modifier.height(48.dp),
        verticalArrangement = Arrangement.Center,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        items(allDaysOfWeek) { item ->
            val isSelected = remember(selectedDaysOfWeek) {
                item in selectedDaysOfWeek
            }
            ItemDayOfWeek(
                dayOfWeek = item,
                isSelected = isSelected,
                onClick = {
                    onDayOfWeekClick(item)
                },
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@Composable
private fun ItemDayOfWeek(
    dayOfWeek: DayOfWeek,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val letter = remember(dayOfWeek) {
        dayOfWeek.toDisplay().take(1)
    }
    Box(
        modifier = modifier
            .clip(MaterialTheme.shapes.small)
            .background(
                if (isSelected) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.surfaceVariant
            )
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = letter,
            style = MaterialTheme.typography.labelLarge,
            color = if (isSelected) MaterialTheme.colorScheme.onPrimary
            else MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(vertical = 8.dp)
        )
    }
}