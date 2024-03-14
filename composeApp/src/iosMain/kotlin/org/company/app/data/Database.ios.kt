package org.company.app.data

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import org.company.app.db.MyDatabase

actual class DriverFactory() {
    actual fun createDriver(): SqlDriver? {
        return NativeSqliteDriver(MyDatabase.Schema, "MyDatabase.db")
    }
}
