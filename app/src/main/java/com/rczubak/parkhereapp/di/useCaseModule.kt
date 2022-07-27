package com.rczubak.parkhereapp.di

import com.rczubak.parkhereapp.domain.usecase.GetCurrentLocationUseCase
import com.rczubak.parkhereapp.domain.usecase.GetLastParkingSpotUseCase
import com.rczubak.parkhereapp.domain.usecase.RemoveCurrentParkingSpotUseCase
import com.rczubak.parkhereapp.domain.usecase.SaveParkingSpotUseCase
import org.koin.dsl.module

val useCaseModule = module {
    single {
        GetCurrentLocationUseCase(get())
    }

    single {
        GetLastParkingSpotUseCase(get())
    }
    single {
        RemoveCurrentParkingSpotUseCase(get())
    }
    single {
        SaveParkingSpotUseCase(get())
    }

}