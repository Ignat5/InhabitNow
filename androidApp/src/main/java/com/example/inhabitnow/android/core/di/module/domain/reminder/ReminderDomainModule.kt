package com.example.inhabitnow.android.core.di.module.domain.reminder

import android.content.Context
import com.example.inhabitnow.android.core.di.qualifier.DefaultDispatcherQualifier
import com.example.inhabitnow.android.platform.manager.DefaultReminderManager
import com.example.inhabitnow.core.platform.ReminderManager
import com.example.inhabitnow.data.repository.reminder.ReminderRepository
import com.example.inhabitnow.data.repository.task.TaskRepository
import com.example.inhabitnow.domain.use_case.reminder.check_reminder_scheduled.CheckReminderScheduledUseCase
import com.example.inhabitnow.domain.use_case.reminder.check_reminder_scheduled.DefaultCheckReminderScheduledUseCase
import com.example.inhabitnow.domain.use_case.reminder.delete_reminder_by_id.DefaultDeleteReminderByIdUseCase
import com.example.inhabitnow.domain.use_case.reminder.delete_reminder_by_id.DeleteReminderByIdUseCase
import com.example.inhabitnow.domain.use_case.reminder.read_reminder_by_id.DefaultReadReminderByIdUseCase
import com.example.inhabitnow.domain.use_case.reminder.read_reminder_by_id.ReadReminderByIdUseCase
import com.example.inhabitnow.domain.use_case.reminder.read_reminders_by_task_id.DefaultReadRemindersByTaskIdUseCase
import com.example.inhabitnow.domain.use_case.reminder.read_reminders_by_task_id.ReadRemindersByTaskIdUseCase
import com.example.inhabitnow.domain.use_case.reminder.read_reminders_count_by_task_id.DefaultReadRemindersCountByTaskIdUseCase
import com.example.inhabitnow.domain.use_case.reminder.read_reminders_count_by_task_id.ReadRemindersCountByTaskIdUseCase
import com.example.inhabitnow.domain.use_case.reminder.reset_task_reminders.DefaultResetTaskRemindersUseCase
import com.example.inhabitnow.domain.use_case.reminder.reset_task_reminders.ResetTaskRemindersUseCase
import com.example.inhabitnow.domain.use_case.reminder.save_reminder.DefaultSaveReminderUseCase
import com.example.inhabitnow.domain.use_case.reminder.save_reminder.SaveReminderUseCase
import com.example.inhabitnow.domain.use_case.reminder.set_up_next_reminder.DefaultSetUpNextReminderUseCase
import com.example.inhabitnow.domain.use_case.reminder.set_up_next_reminder.SetUpNextReminderUseCase
import com.example.inhabitnow.domain.use_case.reminder.set_up_task_reminders.DefaultSetUpTaskRemindersUseCase
import com.example.inhabitnow.domain.use_case.reminder.set_up_task_reminders.SetUpTaskRemindersUseCase
import com.example.inhabitnow.domain.use_case.reminder.update_reminder.DefaultUpdateReminderByIdUseCase
import com.example.inhabitnow.domain.use_case.reminder.update_reminder.UpdateReminderByIdUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
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
    fun provideReadReminderByIdUseCase(
        reminderRepository: ReminderRepository
    ): ReadReminderByIdUseCase {
        return DefaultReadReminderByIdUseCase(
            reminderRepository = reminderRepository
        )
    }

    @Provides
    fun provideReadRemindersCountByTaskIdUseCase(
        reminderRepository: ReminderRepository
    ): ReadRemindersCountByTaskIdUseCase {
        return DefaultReadRemindersCountByTaskIdUseCase(reminderRepository = reminderRepository)
    }

    @Provides
    fun provideSaveReminderUseCase(
        reminderRepository: ReminderRepository,
        setUpNextReminderUseCase: SetUpNextReminderUseCase,
        @DefaultDispatcherQualifier defaultDispatcher: CoroutineDispatcher,
        externalScope: CoroutineScope
    ): SaveReminderUseCase {
        return DefaultSaveReminderUseCase(
            reminderRepository = reminderRepository,
            setUpNextReminderUseCase = setUpNextReminderUseCase,
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

    @Provides
    fun provideUpdateReminderByIdUseCase(
        reminderRepository: ReminderRepository,
        setUpNextReminderUseCase: SetUpNextReminderUseCase,
        @DefaultDispatcherQualifier defaultDispatcher: CoroutineDispatcher,
        externalScope: CoroutineScope
    ): UpdateReminderByIdUseCase {
        return DefaultUpdateReminderByIdUseCase(
            reminderRepository = reminderRepository,
            setUpNextReminderUseCase = setUpNextReminderUseCase,
            defaultDispatcher = defaultDispatcher,
            externalScope = externalScope
        )
    }

    @Provides
    fun provideSetUpTaskRemindersUseCase(
        reminderRepository: ReminderRepository,
        setUpNextReminderUseCase: SetUpNextReminderUseCase,
        @DefaultDispatcherQualifier defaultDispatcher: CoroutineDispatcher,
    ): SetUpTaskRemindersUseCase {
        return DefaultSetUpTaskRemindersUseCase(
            reminderRepository = reminderRepository,
            setUpNextReminderUseCase = setUpNextReminderUseCase,
            defaultDispatcher = defaultDispatcher
        )
    }

    @Provides
    fun provideCheckReminderScheduledUseCase(
        reminderRepository: ReminderRepository,
        taskRepository: TaskRepository,
        @DefaultDispatcherQualifier defaultDispatcher: CoroutineDispatcher
    ): CheckReminderScheduledUseCase {
        return DefaultCheckReminderScheduledUseCase(
            reminderRepository = reminderRepository,
            taskRepository = taskRepository,
            defaultDispatcher = defaultDispatcher
        )
    }

    @Provides
    fun provideSetUpNextReminderUseCase(
        reminderRepository: ReminderRepository,
        taskRepository: TaskRepository,
        reminderManager: ReminderManager,
        @DefaultDispatcherQualifier defaultDispatcher: CoroutineDispatcher
    ): SetUpNextReminderUseCase {
        return DefaultSetUpNextReminderUseCase(
            reminderRepository = reminderRepository,
            taskRepository = taskRepository,
            reminderManager = reminderManager,
            defaultDispatcher = defaultDispatcher
        )
    }

    @Provides
    fun provideResetTaskRemindersUseCase(
        reminderRepository: ReminderRepository,
        reminderManager: ReminderManager,
        @DefaultDispatcherQualifier defaultDispatcher: CoroutineDispatcher
    ): ResetTaskRemindersUseCase {
        return DefaultResetTaskRemindersUseCase(
            reminderRepository = reminderRepository,
            reminderManager = reminderManager,
            defaultDispatcher = defaultDispatcher
        )
    }

    @Provides
    fun provideReminderManager(
        @ApplicationContext context: Context
    ): ReminderManager {
        return DefaultReminderManager(context)
    }

}