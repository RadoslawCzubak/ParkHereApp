package com.rczubak.parkhereapp.features.map

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rczubak.parkhereapp.domain.model.Coordinates
import com.rczubak.parkhereapp.domain.model.ParkingSpot
import com.rczubak.parkhereapp.domain.usecase.GetCurrentLocationUseCase
import com.rczubak.parkhereapp.domain.usecase.GetLastParkingSpotUseCase
import com.rczubak.parkhereapp.domain.usecase.RemoveCurrentParkingSpotUseCase
import com.rczubak.parkhereapp.domain.usecase.SaveParkingSpotUseCase
import kotlinx.coroutines.launch

class MapViewModel(
    private val getCurrentLocationUseCase: GetCurrentLocationUseCase,
    private val getLastParkingSpotUseCase: GetLastParkingSpotUseCase,
    private val saveParkingSpotUseCase: SaveParkingSpotUseCase,
    private val removeCurrentParkingSpotUseCase: RemoveCurrentParkingSpotUseCase
) : ViewModel() {

    private val _lastUserLocation = MutableLiveData<Coordinates>()
    val lastUserLocation: LiveData<Coordinates> = _lastUserLocation

    private val _parkLocation = MutableLiveData<ParkingSpot>(null)
    val parkLocation: LiveData<ParkingSpot?> = _parkLocation

    private val _coordinatesToNavigate = MutableLiveData<Coordinates>()
    val coordinatesToNavigate: LiveData<Coordinates> = _coordinatesToNavigate

    init {
        getSavedParkingSpot()
    }

    private fun getSavedParkingSpot() {
        viewModelScope.launch {
            val parkingSpot = getLastParkingSpotUseCase()
            _parkLocation.postValue(parkingSpot)
        }
    }

    @SuppressLint("MissingPermission")
    fun getUserLocation() {
        try {
            viewModelScope.launch {
                val userLocation = getCurrentLocationUseCase()
                _lastUserLocation.value = userLocation
            }
        } catch (e: SecurityException) {
            Log.e("", "Location error occured", e)
        }
    }


    private fun getUserLocationAndPark() {
        viewModelScope.launch {
            try {
                val location = getCurrentLocationUseCase()
                if (location != null) {
                    _lastUserLocation.value = location
                    if (_parkLocation.value == null) {
                        saveParkingSpotUseCase(
                            location
                        )
                    }
                }
            } catch (e: SecurityException) {
                Log.e("", "Location error occured", e)
            }
            getSavedParkingSpot()
        }
    }

    fun parkHere() {
        getUserLocationAndPark()
    }

    fun endParking() {
        viewModelScope.launch {
            val currentParkingSpot = _parkLocation.value
            if (currentParkingSpot != null) {
                removeCurrentParkingSpotUseCase(currentParkingSpot)
                _parkLocation.value = null
            }
        }
    }

    fun leadToParkLocation() {
        val parkingSpot = parkLocation.value
        if (parkingSpot != null)
            _coordinatesToNavigate.value = parkingSpot.coordinates
    }

}