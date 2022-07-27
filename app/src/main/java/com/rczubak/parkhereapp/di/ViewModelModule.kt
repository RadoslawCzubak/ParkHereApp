package com.rczubak.parkhereapp.di

import com.rczubak.parkhereapp.features.map.MapViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel {
        MapViewModel(get(), get(), get(), get())
    }
}