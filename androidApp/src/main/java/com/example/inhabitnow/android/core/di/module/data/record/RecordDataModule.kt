package com.example.inhabitnow.android.core.di.module.data.record

import com.example.inhabitnow.android.core.di.qualifier.DefaultDispatcherQualifier
import com.example.inhabitnow.android.core.di.qualifier.IODispatcherQualifier
import com.example.inhabitnow.data.data_source.record.DefaultRecordDataSource
import com.example.inhabitnow.data.data_source.record.RecordDataSource
import com.example.inhabitnow.data.repository.record.DefaultRecordRepository
import com.example.inhabitnow.data.repository.record.RecordRepository
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
object RecordDataModule {

    @Provides
    @Singleton
    fun provideRecordDataSource(
        db: InhabitNowDatabase,
        @IODispatcherQualifier ioDispatcher: CoroutineDispatcher
    ): RecordDataSource {
        return DefaultRecordDataSource(
            db = db,
            ioDispatcher = ioDispatcher
        )
    }

    @Provides
    @Singleton
    fun provideRecordRepository(
        recordDataSource: RecordDataSource,
        json: Json,
        @DefaultDispatcherQualifier defaultDispatcher: CoroutineDispatcher
    ): RecordRepository {
        return DefaultRecordRepository(
            recordDataSource = recordDataSource,
            json = json,
            defaultDispatcher = defaultDispatcher
        )
    }

}