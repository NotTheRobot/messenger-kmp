package org.company.app.di

import org.company.app.data.DriverFactory
import org.company.app.data.Repository
import org.company.app.data.RepositoryDefault
import org.company.app.db.MyDatabase
import org.koin.core.module.Module
import org.koin.dsl.module

fun commonModule() = module{
    single<MyDatabase>{
        MyDatabase(driver = get<DriverFactory>().createDriver()!!)
    }

    single<Repository>{
        RepositoryDefault(
            database = get()
        )
    }
}

expect fun platformModule(): Module
