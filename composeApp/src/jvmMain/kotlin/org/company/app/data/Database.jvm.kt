package org.company.app.data

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import org.company.app.db.MyDatabase

actual class DriverFactory() {
    actual fun createDriver(): SqlDriver? {
        val driver: SqlDriver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
        MyDatabase.Schema.create(driver)
        return driver
    }

}
