package com.rczubak.parkhereapp.di

import androidx.room.Room
import com.rczubak.parkhereapp.data.database.room.ParkingDatabase
import com.rczubak.parkhereapp.data.database.sharedPrefs.SharedPreferencesDAO
import com.rczubak.parkhereapp.data.database.sharedPrefs.SharedPreferencesDAOSource
import com.rczubak.parkhereapp.data.repository.ParkingRepositoryImpl
import com.rczubak.parkhereapp.domain.ParkingRepository
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val databaseModule = module {
    single<SharedPreferencesDAOSource> {
        SharedPreferencesDAO(androidContext())
    }

    single {
        Room.databaseBuilder(
            androidApplication(),
            ParkingDatabase::class.java, "pl.rczubak.parkhereapp.parking-database"
        ).build()
    }

    single {
        val database: ParkingDatabase by inject()
        database.parkingDao()
    }

    single<ParkingRepository> {
        ParkingRepositoryImpl(get())
    }
}