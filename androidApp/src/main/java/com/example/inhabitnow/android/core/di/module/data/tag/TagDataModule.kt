package com.example.inhabitnow.android.core.di.module.data.tag

import com.example.inhabitnow.android.core.di.qualifier.DefaultDispatcherQualifier
import com.example.inhabitnow.android.core.di.qualifier.IODispatcherQualifier
import com.example.inhabitnow.data.data_source.tag.DefaultTagDataSource
import com.example.inhabitnow.data.data_source.tag.TagDataSource
import com.example.inhabitnow.data.repository.tag.DefaultTagRepository
import com.example.inhabitnow.data.repository.tag.TagRepository
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
object TagDataModule {

    @Provides
    @Singleton
    fun provideTagDataSource(
        db: InhabitNowDatabase,
        @IODispatcherQualifier ioDispatcher: CoroutineDispatcher
    ): TagDataSource {
        return DefaultTagDataSource(
            db = db,
            ioDispatcher = ioDispatcher
        )
    }

    @Provides
    @Singleton
    fun provideTagRepository(
        tagDataSource: TagDataSource,
        json: Json,
        @DefaultDispatcherQualifier defaultDispatcher: CoroutineDispatcher
    ): TagRepository {
        return DefaultTagRepository(
            tagDataSource = tagDataSource,
            json = json,
            defaultDispatcher = defaultDispatcher
        )
    }

}