package com.rczubak.parkhereapp.di


import android.content.Context
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.rczubak.parkhereapp.data.repository.LocationRepository
import com.rczubak.parkhereapp.data.repository.LocationSource
import com.rczubak.parkhereapp.data.SharedPreferencesDAO
import com.rczubak.parkhereapp.data.SharedPreferencesDAOSource
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

private fun provideFusedLocationProviderClient(context: Context) =
    LocationServices.getFusedLocationProviderClient(context)

private fun provideSharedPreferencesDAO(context: Context) = SharedPreferencesDAO(context)

private fun provideLocationRepository(locationProvider: FusedLocationProviderClient) =
    LocationRepository(locationProvider)

val appModule = module {
    single<SharedPreferencesDAOSource> { provideSharedPreferencesDAO(androidContext()) }
    single { provideFusedLocationProviderClient(androidContext()) }
    single<LocationSource> { provideLocationRepository(get()) }
}