package com.example.inhabitnow.android.presentation.view_habits

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.inhabitnow.android.presentation.base.view_model.BaseViewModel
import com.example.inhabitnow.android.presentation.view_habits.components.ViewHabitsScreenConfig
import com.example.inhabitnow.android.presentation.view_habits.components.ViewHabitsScreenEvent
import com.example.inhabitnow.android.presentation.view_habits.components.ViewHabitsScreenNavigation
import com.example.inhabitnow.android.presentation.view_habits.components.ViewHabitsScreenState
import com.example.inhabitnow.domain.use_case.read_full_habits.ReadFullHabitsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class ViewHabitsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val readFullHabitsUseCase: ReadFullHabitsUseCase
) : BaseViewModel<ViewHabitsScreenEvent, ViewHabitsScreenState, ViewHabitsScreenNavigation, ViewHabitsScreenConfig>() {

    private val allHabitsState = readFullHabitsUseCase()
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            emptyList()
        )

    override val uiScreenState: StateFlow<ViewHabitsScreenState>
        get() = TODO("Not yet implemented")

    override fun onEvent(event: ViewHabitsScreenEvent) {
        TODO("Not yet implemented")
    }

}