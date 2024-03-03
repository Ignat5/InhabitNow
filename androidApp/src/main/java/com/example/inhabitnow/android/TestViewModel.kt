package com.example.inhabitnow.android

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.inhabitnow.database.InhabitNowDatabase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TestViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val db: InhabitNowDatabase
): ViewModel() {
}