package com.example.inhabitnow.domain.use_case.read_full_tasks

import com.example.inhabitnow.domain.model.task.derived.FullTaskModel
import kotlinx.coroutines.flow.Flow

interface ReadFullTasksUseCase {
    operator fun invoke(): Flow<List<FullTaskModel.FullTask>>
}