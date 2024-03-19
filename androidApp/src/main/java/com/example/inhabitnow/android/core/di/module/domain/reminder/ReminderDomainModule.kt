package com.example.inhabitnow.android.core.di.module.domain.reminder

import com.example.inhabitnow.android.core.di.qualifier.DefaultDispatcherQualifier
import com.example.inhabitnow.data.repository.reminder.ReminderRepository
import com.example.inhabitnow.domain.use_case.reminder.delete_reminder_by_id.DefaultDeleteReminderByIdUseCase
import com.example.inhabitnow.domain.use_case.reminder.delete_reminder_by_id.DeleteReminderByIdUseCase
import com.example.inhabitnow.domain.use_case.reminder.read_reminders_by_task_id.DefaultReadRemindersByTaskIdUseCase
import com.example.inhabitnow.domain.use_case.reminder.read_reminders_by_task_id.ReadRemindersByTaskIdUseCase
import com.example.inhabitnow.domain.use_case.reminder.save_reminder.DefaultSaveReminderUseCase
import com.example.inhabitnow.domain.use_case.reminder.save_reminder.SaveReminderUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope

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

    @Provides
    fun provideSaveReminderUseCase(
        reminderRepository: ReminderRepository,
        @DefaultDispatcherQualifier defaultDispatcher: CoroutineDispatcher,
        externalScope: CoroutineScope
    ): SaveReminderUseCase {
        return DefaultSaveReminderUseCase(
            reminderRepository = reminderRepository,
            defaultDispatcher = defaultDispatcher,
            externalScope = externalScope
        )
    }

    @Provides
    fun provideDeleteReminderByIdUseCase(
        reminderRepository: ReminderRepository,
        externalScope: CoroutineScope
    ): DeleteReminderByIdUseCase {
        return DefaultDeleteReminderByIdUseCase(
            reminderRepository = reminderRepository,
            externalScope = externalScope
        )
    }

}