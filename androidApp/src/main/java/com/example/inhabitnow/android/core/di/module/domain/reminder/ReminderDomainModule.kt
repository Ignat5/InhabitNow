package com.example.inhabitnow.android.core.di.module.domain.reminder

import com.example.inhabitnow.android.core.di.qualifier.DefaultDispatcherQualifier
import com.example.inhabitnow.data.repository.reminder.ReminderRepository
import com.example.inhabitnow.domain.use_case.reminder.read_reminders_by_task_id.DefaultReadRemindersByTaskIdUseCase
import com.example.inhabitnow.domain.use_case.reminder.read_reminders_by_task_id.ReadRemindersByTaskIdUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher

@Module
@InstallIn(SingletonComponent::class)
object ReminderDomainModule {

    @Provides
    fun provideReadRemindersByTaskIdUseCase(
        reminderRepository: ReminderRepository,
        @DefaultDispatcherQualifier defaultDispatcher: CoroutineDispatcher
    ): ReadRemindersByTaskIdUseCase {
        return DefaultReadRemindersByTaskIdUseCase(
            reminderRepository = reminderRepository,
            defaultDispatcher = defaultDispatcher
        )
    }

}