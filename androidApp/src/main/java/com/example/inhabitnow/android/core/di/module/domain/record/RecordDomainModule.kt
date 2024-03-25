package com.example.inhabitnow.android.core.di.module.domain.record

import com.example.inhabitnow.android.core.di.qualifier.DefaultDispatcherQualifier
import com.example.inhabitnow.data.repository.record.RecordRepository
import com.example.inhabitnow.domain.use_case.record.read_records_by_date.DefaultReadRecordsByDateUseCase
import com.example.inhabitnow.domain.use_case.record.read_records_by_date.ReadRecordsByDateUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher

@Module
@InstallIn(SingletonComponent::class)
object RecordDomainModule {

    @Provides
    fun provideReadRecordsByDateUseCase(
        recordRepository: RecordRepository,
        @DefaultDispatcherQualifier defaultDispatcher: CoroutineDispatcher
    ): ReadRecordsByDateUseCase {
        return DefaultReadRecordsByDateUseCase(
            recordRepository = recordRepository,
            defaultDispatcher = defaultDispatcher
        )
    }

}