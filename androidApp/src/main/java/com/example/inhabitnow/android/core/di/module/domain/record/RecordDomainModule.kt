package com.example.inhabitnow.android.core.di.module.domain.record

import com.example.inhabitnow.data.repository.record.RecordRepository
import com.example.inhabitnow.domain.record.DefaultRecordDomain
import com.example.inhabitnow.domain.record.RecordDomain
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RecordDomainModule {

    @Provides
    @Singleton
    fun provideRecordDomain(recordRepository: RecordRepository): RecordDomain {
        return DefaultRecordDomain(recordRepository = recordRepository)
    }

}