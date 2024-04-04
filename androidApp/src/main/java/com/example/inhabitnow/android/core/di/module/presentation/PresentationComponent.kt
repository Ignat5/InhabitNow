package com.example.inhabitnow.android.core.di.module.presentation

import com.example.inhabitnow.android.App
import com.example.inhabitnow.domain.use_case.read_task_with_content_by_id.ReadTaskWithContentByIdUseCase
import com.example.inhabitnow.domain.use_case.reminder.check_reminder_scheduled.CheckReminderScheduledUseCase
import com.example.inhabitnow.domain.use_case.reminder.read_reminder_by_id.ReadReminderByIdUseCase
import com.example.inhabitnow.domain.use_case.reminder.set_up_next_reminder.SetUpNextReminderUseCase
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope

object PresentationComponent {

    private val entryPoint by lazy {
        EntryPointAccessors.fromApplication(
            App.appContext,
            PlatformComponentsEntryPoint::class.java
        )
    }

    val setUpNextReminderUseCase: SetUpNextReminderUseCase
        get() = entryPoint.provideSetUpNextReminderUseCase()

    val checkReminderScheduledUseCase: CheckReminderScheduledUseCase
        get() = entryPoint.provideCheckReminderScheduledUseCase()

    val readReminderByIdUseCase: ReadReminderByIdUseCase
        get() = entryPoint.provideReadReminderByIdUseCase()

    val readTaskWithContentByIdUseCase: ReadTaskWithContentByIdUseCase
        get() = entryPoint.provideReadTaskWithContentByIdUseCase()

    val externalScope: CoroutineScope
        get() = entryPoint.provideExternalScope()

    @EntryPoint
    @InstallIn(SingletonComponent::class)
    interface PlatformComponentsEntryPoint {
        fun provideSetUpNextReminderUseCase(): SetUpNextReminderUseCase
        fun provideCheckReminderScheduledUseCase(): CheckReminderScheduledUseCase
        fun provideReadReminderByIdUseCase(): ReadReminderByIdUseCase
        fun provideReadTaskWithContentByIdUseCase(): ReadTaskWithContentByIdUseCase
        fun provideExternalScope(): CoroutineScope
    }

}