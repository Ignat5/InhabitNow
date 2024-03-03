package com.example.inhabitnow.android.core.di.module

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import com.example.inhabitnow.database.InhabitNowDatabase
import com.example.inhabitnow.db.AndroidSqlDriverBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CoreDataModule {

    @Provides
    @Singleton
    fun provideDatabase(sqlDriver: SqlDriver): InhabitNowDatabase {
        return InhabitNowDatabase.invoke(sqlDriver)
    }

    @Provides
    @Singleton
    fun provideSqlDriver(@ApplicationContext context: Context): SqlDriver {
        return AndroidSqlDriverBuilder(context).build()
    }

}