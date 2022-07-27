package com.rczubak.parkhereapp.di

import com.google.android.gms.location.LocationServices
import com.rczubak.parkhereapp.data.repository.LocationRepositoryImpl
import com.rczubak.parkhereapp.domain.LocationRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val locationModule = module {
    single {
        LocationServices.getFusedLocationProviderClient(androidContext())
    }

    single<LocationRepository> {
        LocationRepositoryImpl(get())
    }
}