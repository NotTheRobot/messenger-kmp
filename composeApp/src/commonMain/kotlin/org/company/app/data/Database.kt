package org.company.app.data

import app.cash.sqldelight.db.SqlDriver
import org.company.app.db.MyDatabase

expect class DriverFactory {
    fun createDriver(): SqlDriver?
}

