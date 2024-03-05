package com.example.inhabitnow.android.core.di.module.data.reminder

import com.example.inhabitnow.android.core.di.qualifier.DefaultDispatcherQualifier
import com.example.inhabitnow.android.core.di.qualifier.IODispatcherQualifier
import com.example.inhabitnow.data.data_source.reminder.DefaultReminderDataSource
import com.example.inhabitnow.data.data_source.reminder.ReminderDataSource
import com.example.inhabitnow.data.repository.reminder.DefaultReminderRepository
import com.example.inhabitnow.data.repository.reminder.ReminderRepository
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
object ReminderDataModule {

    @Provides
    @Singleton
    fun provideReminderDataSource(
        db: InhabitNowDatabase,
        @IODispatcherQualifier ioDispatcher: CoroutineDispatcher
    ): ReminderDataSource {
        return DefaultReminderDataSource(
            db = db,
            ioDispatcher = ioDispatcher
        )
    }

    @Provides
    @Singleton
    fun provideReminderRepository(
        reminderDataSource: ReminderDataSource,
        json: Json,
        @DefaultDispatcherQualifier defaultDispatcher: CoroutineDispatcher
    ): ReminderRepository {
        return DefaultReminderRepository(
            reminderDataSource = reminderDataSource,
            json = json,
            defaultDispatcher = defaultDispatcher
        )
    }

}