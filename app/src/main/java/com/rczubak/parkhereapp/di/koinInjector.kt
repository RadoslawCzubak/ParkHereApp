package com.rczubak.parkhereapp.di

import org.koin.core.module.Module

val koinInjector: List<Module> = listOf(viewModelModule)
    .plus(locationModule)
    .plus(databaseModule)