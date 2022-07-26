package com.rczubak.parkhereapp.di

import com.rczubak.parkhereapp.data.SharedPreferencesDAO
import com.rczubak.parkhereapp.data.SharedPreferencesDAOSource
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val databaseModule = module {
    single<SharedPreferencesDAOSource> {
        SharedPreferencesDAO(androidContext())
    }
}