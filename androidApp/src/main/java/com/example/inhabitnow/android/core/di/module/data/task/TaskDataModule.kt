package com.example.inhabitnow.android.core.di.module.data.task

import com.example.inhabitnow.android.core.di.qualifier.DefaultDispatcherQualifier
import com.example.inhabitnow.android.core.di.qualifier.IODispatcherQualifier
import com.example.inhabitnow.data.data_source.task.DefaultTaskDataSource
import com.example.inhabitnow.data.data_source.task.TaskDataSource
import com.example.inhabitnow.data.repository.task.DefaultTaskRepository
import com.example.inhabitnow.data.repository.task.TaskRepository
import com.example.inhabitnow.database.InhabitNowDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.serialization.json.Json
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TaskDataModule {

    @Provides
    @Singleton
    fun provideTaskDataSource(
        db: InhabitNowDatabase,
        @IODispatcherQualifier ioDispatcher: CoroutineDispatcher
    ): TaskDataSource {
        return DefaultTaskDataSource(
            db = db,
            ioDispatcher = ioDispatcher
        )
    }

    @Provides
    @Singleton
    fun provideTaskRepository(
        taskDataSource: TaskDataSource,
        json: Json,
        @DefaultDispatcherQualifier defaultDispatcher: CoroutineDispatcher
    ): TaskRepository {
        return DefaultTaskRepository(
            taskDataSource = taskDataSource,
            json = json,
            defaultDispatcher = defaultDispatcher
        )
    }

}