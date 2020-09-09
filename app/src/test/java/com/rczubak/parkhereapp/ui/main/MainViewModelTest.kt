package com.rczubak.parkhereapp.ui.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.android.gms.maps.model.LatLng
import com.rczubak.parkhereapp.data.repository.FakeLocationRepository
import com.rczubak.parkhereapp.data.repository.FakeSharedPrefsDAO
import com.rczubak.parkhereapp.getOrAwaitValue
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test


class MainViewModelTest{

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Test
    fun locationToLatLng_noLocation_null(){
        //Given
        val location = null

        val viewModel = MainViewModel(FakeLocationRepository(location), FakeSharedPrefsDAO())
        viewModel.locationPermissionGranted()
        //When
        viewModel.getUserLocation()

        //Then
        val value = viewModel.lastUserLocation.getOrAwaitValue()
        assertNull(value)
    }

    @Test
    fun locationToLatLng_UserLocated_location(){
        //Given
        val location = LatLng(15.0,16.33)

        val viewModel = MainViewModel(FakeLocationRepository(location), FakeSharedPrefsDAO())
        viewModel.locationPermissionGranted()
        //When
        viewModel.getUserLocation()

        //Then
        val value = viewModel.lastUserLocation.getOrAwaitValue()
        assertEquals(value.latitude,15.0, 0.0)
        assertEquals(value.longitude,16.33, 0.0)
    }

}