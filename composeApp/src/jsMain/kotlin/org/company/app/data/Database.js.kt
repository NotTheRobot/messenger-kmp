package org.company.app.data

import app.cash.sqldelight.db.SqlDriver

actual class DriverFactory() {
    actual fun createDriver(): SqlDriver? {
        return null
    }
}
