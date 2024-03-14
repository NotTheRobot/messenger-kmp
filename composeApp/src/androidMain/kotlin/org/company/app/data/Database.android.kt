package org.company.app.data

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import org.company.app.db.MyDatabase

actual class DriverFactory (private val context: Context) {
    actual fun createDriver(): SqlDriver? {
        return AndroidSqliteDriver(MyDatabase.Schema, context, "MyDatabase.db")
    }
}
