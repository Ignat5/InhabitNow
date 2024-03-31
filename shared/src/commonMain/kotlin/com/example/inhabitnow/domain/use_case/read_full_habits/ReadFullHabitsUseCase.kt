package com.example.inhabitnow.domain.use_case.read_full_habits

import com.example.inhabitnow.domain.model.task.derived.FullTaskModel
import kotlinx.coroutines.flow.Flow

interface ReadFullHabitsUseCase {
    operator fun invoke(): Flow<List<FullTaskModel.FullHabit>>
}