package com.example.inhabitnow.android.presentation.create_edit_task.common.config.pick_frequency

import com.example.inhabitnow.android.presentation.base.state_holder.BaseResultStateHolder
import com.example.inhabitnow.android.presentation.create_edit_task.common.config.pick_frequency.components.PickTaskFrequencyScreenEvent
import com.example.inhabitnow.android.presentation.create_edit_task.common.config.pick_frequency.components.PickTaskFrequencyScreenResult
import com.example.inhabitnow.android.presentation.create_edit_task.common.config.pick_frequency.components.PickTaskFrequencyScreenState
import com.example.inhabitnow.android.presentation.model.SelectableDayOfWeek
import com.example.inhabitnow.android.presentation.model.UITaskContent
import com.example.inhabitnow.android.ui.toUIFrequencyContent
import com.example.inhabitnow.domain.model.task.content.TaskContentModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.datetime.DayOfWeek

class PickTaskFrequencyStateHolder(
    initFrequency: UITaskContent.Frequency,
    override val holderScope: CoroutineScope
) : BaseResultStateHolder<PickTaskFrequencyScreenEvent, PickTaskFrequencyScreenState, PickTaskFrequencyScreenResult>() {

    private val uiFrequencyContentState = MutableStateFlow(initFrequency)

    override val uiScreenState: StateFlow<PickTaskFrequencyScreenState> =
        uiFrequencyContentState.map { uiFrequencyContent ->
            provideScreenState(
                uiFrequencyContent = uiFrequencyContent
            )
        }.stateIn(
            holderScope,
            SharingStarted.WhileSubscribed(5000L),
            provideScreenState(
                uiFrequencyContent = uiFrequencyContentState.value
            )
        )

    override fun onEvent(event: PickTaskFrequencyScreenEvent) {
        when (event) {
            is PickTaskFrequencyScreenEvent.OnFrequencyTypeClick ->
                onFrequencyTypeClick(event)

            is PickTaskFrequencyScreenEvent.OnDayOfWeekClick -> onDayOfWeekClick(event)
            is PickTaskFrequencyScreenEvent.OnConfirmClick -> onConfirmClick()
            is PickTaskFrequencyScreenEvent.OnDismissRequest -> onDismissRequest()
        }
    }

    private fun onConfirmClick() {
        if (uiScreenState.value.canConfirm) {
            setUpResult(PickTaskFrequencyScreenResult.Confirm(uiFrequencyContentState.value))
        }
    }

    private fun onDismissRequest() {
        setUpResult(PickTaskFrequencyScreenResult.Dismiss)
    }

    private fun onDayOfWeekClick(event: PickTaskFrequencyScreenEvent.OnDayOfWeekClick) {
        (uiFrequencyContentState.value as? UITaskContent.Frequency.DaysOfWeek)?.let { prev ->
            uiFrequencyContentState.update {
                val clickedDayOfWeek = event.dayOfWeek
                val oldSet = prev.daysOfWeek
                val newSet = mutableSetOf<DayOfWeek>()
                newSet.addAll(oldSet)
                if (newSet.contains(clickedDayOfWeek)) newSet.remove(clickedDayOfWeek)
                else newSet.add(clickedDayOfWeek)
                UITaskContent.Frequency.DaysOfWeek(newSet)
            }
        }
    }

    private fun onFrequencyTypeClick(event: PickTaskFrequencyScreenEvent.OnFrequencyTypeClick) {
        val clickedType = event.type
        if (uiFrequencyContentState.value.type != clickedType) {
            uiFrequencyContentState.update {
                when (clickedType) {
                    UITaskContent.Frequency.Type.EveryDay -> {
                        UITaskContent.Frequency.EveryDay
                    }

                    UITaskContent.Frequency.Type.DaysOfWeek -> {
                        UITaskContent.Frequency.DaysOfWeek(emptySet())
                    }
                }
            }
        }
    }

    private fun provideScreenState(
        uiFrequencyContent: UITaskContent.Frequency
    ): PickTaskFrequencyScreenState {
        return PickTaskFrequencyScreenState(
            uiFrequencyContent = uiFrequencyContent,
            canConfirm = when (uiFrequencyContent) {
                is UITaskContent.Frequency.EveryDay -> true
                is UITaskContent.Frequency.DaysOfWeek -> uiFrequencyContent.daysOfWeek.isNotEmpty()
            }
        )
    }

}