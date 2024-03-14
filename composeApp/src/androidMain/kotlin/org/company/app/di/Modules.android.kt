package org.company.app.di

import org.company.app.data.DriverFactory
import org.koin.core.module.Module
import org.koin.dsl.module

actual fun platformModule(): Module = module{
    single{ DriverFactory(context = get()) }
}
