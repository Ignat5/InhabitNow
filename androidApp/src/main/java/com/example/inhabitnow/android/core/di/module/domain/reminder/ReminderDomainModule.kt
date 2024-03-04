package com.example.inhabitnow.android.core.di.module.domain.reminder

import com.example.inhabitnow.android.core.di.qualifier.DefaultDispatcherQualifier
import com.example.inhabitnow.data.repository.reminder.ReminderRepository
import com.example.inhabitnow.domain.reminder.DefaultReminderDomain
import com.example.inhabitnow.domain.reminder.ReminderDomain
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ReminderDomainModule {

    @Provides
    @Singleton
    fun provideReminderDomain(
        reminderRepository: ReminderRepository,
        @DefaultDispatcherQualifier defaultDispatcher: CoroutineDispatcher
    ): ReminderDomain {
        return DefaultReminderDomain(
            reminderRepository = reminderRepository,
            defaultDispatcher = defaultDispatcher
        )
    }

}