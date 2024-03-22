package com.example.inhabitnow.data.data_source.base

import app.cash.sqldelight.Query
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOne
import com.example.inhabitnow.core.model.ResultModel
import com.example.inhabitnow.database.InhabitNowDatabase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

abstract class BaseDataSource(
    private val db: InhabitNowDatabase,
    private val ioDispatcher: CoroutineDispatcher
) {

    protected fun <T : Any> readQueryList(query: () -> Query<T>): Flow<List<T>> =
        query()
            .asFlow()
            .mapToList(ioDispatcher)

    protected fun <T : Any> readQuery(query: () -> Query<T>): Flow<T> =
        query()
            .asFlow()
            .mapToOne(ioDispatcher)

    protected suspend fun <T : Any> getOneOrNull(query: () -> Query<T>): T? =
        try {
            withContext(ioDispatcher) {
                query()
                    .executeAsOneOrNull()
            }
        } catch (e: Exception) {
            null
        }


    protected suspend fun <T : Any> runQuery(query: () -> T): ResultModel<T> =
        try {
            withContext(ioDispatcher) {
                ResultModel.Success(query())
            }
        } catch (e: Exception) {
            ResultModel.Error(e)
        }

    protected suspend fun runTransaction(query: () -> Unit): ResultModel<Unit> =
        try {
            withContext(ioDispatcher) {
                suspendCoroutine { cnt ->
                    db.transaction {
                        this.afterCommit { cnt.resume(Unit) }
                        this.afterRollback { cnt.resumeWithException(IllegalStateException()) }
                        query()
                    }
                }
                ResultModel.Success(Unit)
            }
        } catch (e: Exception) {
            ResultModel.Error(e)
        }

}