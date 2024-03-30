package com.example.inhabitnow.domain.use_case.read_full_habits

import com.example.inhabitnow.core.type.TaskType
import com.example.inhabitnow.data.repository.task.TaskRepository
import com.example.inhabitnow.domain.model.task.derived.FullTaskModel
import com.example.inhabitnow.domain.util.toFullTaskModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class DefaultReadFullHabitsUseCase(
    private val taskRepository: TaskRepository,
    private val defaultDispatcher: CoroutineDispatcher
) : ReadFullHabitsUseCase {

    override operator fun invoke(): Flow<List<FullTaskModel.FullHabit>> =
        taskRepository.readFullTasksByType(setOf(TaskType.Habit)).map { allTasks ->
            if (allTasks.isNotEmpty()) {
                withContext(defaultDispatcher) {
                    allTasks.map {
                        async {
                            it.toFullTaskModel()
                        }
                    }.awaitAll().filterIsInstance<FullTaskModel.FullHabit>()
                }
            } else emptyList()
        }

}