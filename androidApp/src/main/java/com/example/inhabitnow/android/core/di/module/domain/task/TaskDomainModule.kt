package com.example.inhabitnow.android.core.di.module.domain.task

import com.example.inhabitnow.android.core.di.qualifier.DefaultDispatcherQualifier
import com.example.inhabitnow.data.repository.record.RecordRepository
import com.example.inhabitnow.data.repository.task.TaskRepository
import com.example.inhabitnow.domain.use_case.archive_task_by_id.ArchiveTaskByIdUseCase
import com.example.inhabitnow.domain.use_case.archive_task_by_id.DefaultArchiveTaskByIdUseCase
import com.example.inhabitnow.domain.use_case.calculate_statistics.CalculateStatisticsUseCase
import com.example.inhabitnow.domain.use_case.calculate_statistics.DefaultCalculateStatisticsUseCase
import com.example.inhabitnow.domain.use_case.delete_task_by_id.DefaultDeleteTaskByIdUseCase
import com.example.inhabitnow.domain.use_case.delete_task_by_id.DeleteTaskByIdUseCase
import com.example.inhabitnow.domain.use_case.read_full_habits.DefaultReadFullHabitsUseCase
import com.example.inhabitnow.domain.use_case.read_full_habits.ReadFullHabitsUseCase
import com.example.inhabitnow.domain.use_case.read_full_tasks.DefaultReadFullTasksUseCase
import com.example.inhabitnow.domain.use_case.read_full_tasks.ReadFullTasksUseCase
import com.example.inhabitnow.domain.use_case.read_full_tasks_by_date.DefaultReadFullTasksByDateUseCase
import com.example.inhabitnow.domain.use_case.read_full_tasks_by_date.ReadFullTasksByDateUseCase
import com.example.inhabitnow.domain.use_case.read_task_with_content_by_id.DefaultReadTaskWithContentByIdUseCase
import com.example.inhabitnow.domain.use_case.read_task_with_content_by_id.ReadTaskWithContentByIdUseCase
import com.example.inhabitnow.domain.use_case.read_tasks_by_search_query.DefaultReadTasksBySearchQueryUseCase
import com.example.inhabitnow.domain.use_case.read_tasks_by_search_query.ReadTasksBySearchQueryUseCase
import com.example.inhabitnow.domain.use_case.restart_habit_by_id.DefaultRestartHabitByIdUseCase
import com.example.inhabitnow.domain.use_case.restart_habit_by_id.RestartHabitByIdUseCase
import com.example.inhabitnow.domain.use_case.save_default_task.DefaultSaveDefaultTaskUseCase
import com.example.inhabitnow.domain.use_case.save_default_task.SaveDefaultTaskUseCase
import com.example.inhabitnow.domain.use_case.save_task_by_id.DefaultSaveTaskByIdUseCase
import com.example.inhabitnow.domain.use_case.save_task_by_id.SaveTaskByIdUseCase
import com.example.inhabitnow.domain.use_case.update_task_date.DefaultUpdateTaskDateUseCase
import com.example.inhabitnow.domain.use_case.update_task_date.UpdateTaskDateUseCase
import com.example.inhabitnow.domain.use_case.update_task_description.DefaultUpdateTaskDescriptionByIdUseCase
import com.example.inhabitnow.domain.use_case.update_task_description.UpdateTaskDescriptionByIdUseCase
import com.example.inhabitnow.domain.use_case.update_task_frequency_by_id.DefaultUpdateTaskFrequencyByIdUseCase
import com.example.inhabitnow.domain.use_case.update_task_frequency_by_id.UpdateTaskFrequencyByIdUseCase
import com.example.inhabitnow.domain.use_case.update_task_priority_by_id.DefaultUpdateTaskPriorityByIdUseCase
import com.example.inhabitnow.domain.use_case.update_task_priority_by_id.UpdateTaskPriorityByIdUseCase
import com.example.inhabitnow.domain.use_case.update_task_progress_by_id.DefaultUpdateTaskProgressByIdUseCase
import com.example.inhabitnow.domain.use_case.update_task_progress_by_id.UpdateTaskProgressByIdUseCase
import com.example.inhabitnow.domain.use_case.update_task_title_by_id.DefaultUpdateTaskTitleByIdUseCase
import com.example.inhabitnow.domain.use_case.update_task_title_by_id.UpdateTaskTitleByIdUseCase
import com.example.inhabitnow.domain.use_case.validate_limit_number.DefaultValidateInputLimitNumberUseCase
import com.example.inhabitnow.domain.use_case.validate_limit_number.ValidateInputLimitNumberUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope

@Module
@InstallIn(SingletonComponent::class)
object TaskDomainModule {

    @Provides
    fun provideSaveDefaultTaskUseCase(
        taskRepository: TaskRepository,
        @DefaultDispatcherQualifier defaultDispatcher: CoroutineDispatcher
    ): SaveDefaultTaskUseCase {
        return DefaultSaveDefaultTaskUseCase(
            taskRepository = taskRepository,
            defaultDispatcher = defaultDispatcher
        )
    }

    @Provides
    fun provideReadTaskWithContentByIdUseCase(
        taskRepository: TaskRepository,
        @DefaultDispatcherQualifier defaultDispatcher: CoroutineDispatcher
    ): ReadTaskWithContentByIdUseCase {
        return DefaultReadTaskWithContentByIdUseCase(
            taskRepository = taskRepository,
            defaultDispatcher = defaultDispatcher
        )
    }

    @Provides
    fun provideReadTasksBySearchQueryUseCase(
        taskRepository: TaskRepository,
        @DefaultDispatcherQualifier defaultDispatcher: CoroutineDispatcher
    ): ReadTasksBySearchQueryUseCase {
        return DefaultReadTasksBySearchQueryUseCase(
            taskRepository = taskRepository,
            defaultDispatcher = defaultDispatcher
        )
    }

    @Provides
    fun provideReadFullTasksByDateUseCase(
        taskRepository: TaskRepository,
        @DefaultDispatcherQualifier defaultDispatcher: CoroutineDispatcher
    ): ReadFullTasksByDateUseCase {
        return DefaultReadFullTasksByDateUseCase(
            taskRepository = taskRepository,
            defaultDispatcher = defaultDispatcher
        )
    }

    @Provides
    fun provideReadFullHabitsUseCase(
        taskRepository: TaskRepository,
        @DefaultDispatcherQualifier defaultDispatcher: CoroutineDispatcher
    ): ReadFullHabitsUseCase {
        return DefaultReadFullHabitsUseCase(
            taskRepository = taskRepository,
            defaultDispatcher = defaultDispatcher
        )
    }

    @Provides
    fun provideReadFullTasksUseCase(
        taskRepository: TaskRepository,
        @DefaultDispatcherQualifier defaultDispatcher: CoroutineDispatcher
    ): ReadFullTasksUseCase {
        return DefaultReadFullTasksUseCase(
            taskRepository = taskRepository,
            defaultDispatcher = defaultDispatcher
        )
    }

    @Provides
    fun provideUpdateTaskTitleByIdUseCase(
        taskRepository: TaskRepository
    ): UpdateTaskTitleByIdUseCase {
        return DefaultUpdateTaskTitleByIdUseCase(
            taskRepository = taskRepository
        )
    }

    @Provides
    fun provideUpdateTaskProgressByIdUseCase(
        taskRepository: TaskRepository,
        @DefaultDispatcherQualifier defaultDispatcher: CoroutineDispatcher
    ): UpdateTaskProgressByIdUseCase {
        return DefaultUpdateTaskProgressByIdUseCase(
            taskRepository = taskRepository,
            defaultDispatcher = defaultDispatcher
        )
    }

    @Provides
    fun provideUpdateTaskFrequencyByIdUseCase(
        taskRepository: TaskRepository,
        @DefaultDispatcherQualifier defaultDispatcher: CoroutineDispatcher,
        externalScope: CoroutineScope
    ): UpdateTaskFrequencyByIdUseCase {
        return DefaultUpdateTaskFrequencyByIdUseCase(
            taskRepository = taskRepository,
            defaultDispatcher = defaultDispatcher,
            externalScope = externalScope
        )
    }

    @Provides
    fun provideUpdateTaskDateByIdUseCase(
        taskRepository: TaskRepository,
        externalScope: CoroutineScope
    ): UpdateTaskDateUseCase {
        return DefaultUpdateTaskDateUseCase(
            taskRepository = taskRepository,
            externalScope = externalScope
        )
    }

    @Provides
    fun provideUpdateTaskDescriptionByIdUseCase(
        taskRepository: TaskRepository
    ): UpdateTaskDescriptionByIdUseCase {
        return DefaultUpdateTaskDescriptionByIdUseCase(
            taskRepository = taskRepository
        )
    }

    @Provides
    fun provideUpdateTaskPriorityByIdUseCase(
        taskRepository: TaskRepository
    ): UpdateTaskPriorityByIdUseCase {
        return DefaultUpdateTaskPriorityByIdUseCase(
            taskRepository = taskRepository
        )
    }

    @Provides
    fun provideSaveTaskByIdUseCase(
        taskRepository: TaskRepository,
        externalScope: CoroutineScope
    ): SaveTaskByIdUseCase {
        return DefaultSaveTaskByIdUseCase(
            taskRepository = taskRepository,
            externalScope = externalScope
        )
    }

    @Provides
    fun provideDeleteTaskByIdUseCase(
        taskRepository: TaskRepository,
        externalScope: CoroutineScope
    ): DeleteTaskByIdUseCase {
        return DefaultDeleteTaskByIdUseCase(
            taskRepository = taskRepository,
            externalScope = externalScope
        )
    }

    @Provides
    fun provideArchiveTaskByIdUseCase(
        taskRepository: TaskRepository,
        externalScope: CoroutineScope
    ): ArchiveTaskByIdUseCase {
        return DefaultArchiveTaskByIdUseCase(
            taskRepository = taskRepository,
            externalScope = externalScope
        )
    }

    @Provides
    fun provideRestartHabitByIdUseCase(
        taskRepository: TaskRepository,
        recordRepository: RecordRepository,
        externalScope: CoroutineScope
    ): RestartHabitByIdUseCase {
        return DefaultRestartHabitByIdUseCase(
            taskRepository = taskRepository,
            recordRepository = recordRepository,
            externalScope = externalScope
        )
    }

    @Provides
    fun provideCalculateStatisticsUseCase(
        taskRepository: TaskRepository,
        recordRepository: RecordRepository,
        @DefaultDispatcherQualifier defaultDispatcher: CoroutineDispatcher
    ): CalculateStatisticsUseCase {
        return DefaultCalculateStatisticsUseCase(
            taskRepository = taskRepository,
            recordRepository = recordRepository,
            defaultDispatcher = defaultDispatcher
        )
    }

    @Provides
    fun provideValidateInputLimitNumberUseCase(): ValidateInputLimitNumberUseCase {
        return DefaultValidateInputLimitNumberUseCase()
    }

}