package com.example.inhabitnow.android.core.di.module.domain.task

import com.example.inhabitnow.android.core.di.qualifier.DefaultDispatcherQualifier
import com.example.inhabitnow.data.repository.task.TaskRepository
import com.example.inhabitnow.domain.use_case.read_task_with_content_by_id.DefaultReadTaskWithContentByIdUseCase
import com.example.inhabitnow.domain.use_case.read_task_with_content_by_id.ReadTaskWithContentByIdUseCase
import com.example.inhabitnow.domain.use_case.save_default_task.DefaultSaveDefaultTaskUseCase
import com.example.inhabitnow.domain.use_case.save_default_task.SaveDefaultTaskUseCase
import com.example.inhabitnow.domain.use_case.update_task_progress_by_id.DefaultUpdateTaskProgressById
import com.example.inhabitnow.domain.use_case.update_task_progress_by_id.UpdateTaskProgressById
import com.example.inhabitnow.domain.use_case.update_task_title_by_id.DefaultUpdateTaskTitleByIdUseCase
import com.example.inhabitnow.domain.use_case.update_task_title_by_id.UpdateTaskTitleByIdUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher

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
    ): UpdateTaskProgressById {
        return DefaultUpdateTaskProgressById(
            taskRepository = taskRepository,
            defaultDispatcher = defaultDispatcher
        )
    }

}