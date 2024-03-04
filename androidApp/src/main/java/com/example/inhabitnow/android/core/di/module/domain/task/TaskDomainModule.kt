package com.example.inhabitnow.android.core.di.module.domain.task

import com.example.inhabitnow.android.core.di.qualifier.DefaultDispatcherQualifier
import com.example.inhabitnow.data.repository.task.TaskRepository
import com.example.inhabitnow.domain.task.DefaultTaskDomain
import com.example.inhabitnow.domain.task.TaskDomain
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TaskDomainModule {

    @Provides
    @Singleton
    fun provideTaskDomain(
        taskRepository: TaskRepository,
        @DefaultDispatcherQualifier defaultDispatcher: CoroutineDispatcher
    ): TaskDomain {
        return DefaultTaskDomain(
            taskRepository = taskRepository,
            defaultDispatcher = defaultDispatcher
        )
    }

}