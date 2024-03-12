package com.example.inhabitnow.android.core.di.module.domain.task

import com.example.inhabitnow.android.core.di.qualifier.DefaultDispatcherQualifier
import com.example.inhabitnow.data.repository.task.TaskRepository
import com.example.inhabitnow.domain.use_case.save_default_task.DefaultSaveDefaultTaskUseCase
import com.example.inhabitnow.domain.use_case.save_default_task.SaveDefaultTaskUseCase
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

}