package com.rczubak.parkhereapp.di

import com.google.android.gms.location.LocationServices
import com.rczubak.parkhereapp.data.repository.LocationRepository
import com.rczubak.parkhereapp.data.repository.LocationSource
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val locationModule = module {
    single {
        LocationServices.getFusedLocationProviderClient(androidContext())
    }

    single<LocationSource> {
        LocationRepository(get())
    }
}