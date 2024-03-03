package com.example.inhabitnow.db

import android.content.Context
import androidx.sqlite.db.SupportSQLiteDatabase
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.example.inhabitnow.core.util.CoreUtil
import com.example.inhabitnow.database.InhabitNowDatabase

class AndroidSqlDriverBuilder(private val context: Context) {
    fun build(): SqlDriver = AndroidSqliteDriver(
        schema = InhabitNowDatabase.Schema,
        context = context,
        name = CoreUtil.DATABASE_NAME,
        callback = object : AndroidSqliteDriver.Callback(InhabitNowDatabase.Schema) {
            override fun onOpen(db: SupportSQLiteDatabase) {
                super.onOpen(db)
                db.setForeignKeyConstraintsEnabled(true)
            }
        }
    )

}