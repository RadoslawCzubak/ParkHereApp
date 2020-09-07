package com.rczubak.parkhereapp.di


import android.content.Context
import com.google.android.gms.location.LocationServices
import com.rczubak.parkhereapp.data.SharedPreferencesDAO
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

private fun provideFusedLocationProviderClient(context: Context) =
    LocationServices.getFusedLocationProviderClient(context)

private fun provideSharedPreferencesDAO(context: Context) = SharedPreferencesDAO(context)

val appModule = module {
    single { provideSharedPreferencesDAO(androidContext()) }
    single { provideFusedLocationProviderClient(androidContext()) }
}