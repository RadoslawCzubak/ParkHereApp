package com.rczubak.parkhereapp.ui.main

import android.annotation.SuppressLint
import android.location.Location
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.model.LatLng
import com.rczubak.parkhereapp.data.SharedPreferencesDAO
import com.rczubak.parkhereapp.data.SharedPreferencesDAOSource
import com.rczubak.parkhereapp.data.repository.LocationRepository
import com.rczubak.parkhereapp.data.repository.LocationSource
import com.rczubak.parkhereapp.utils.PARK_LOCATION_KEY
import com.rczubak.parkhereapp.utils.locationToLatLng

class MainViewModel(
    private val locationRepository: LocationSource,
    private val sharedPreferencesDAO: SharedPreferencesDAOSource
) : ViewModel() {

    private val _locationPermission = MutableLiveData<Boolean>(false)
    val locationPermission: LiveData<Boolean> = _locationPermission

    private val _lastUserLocation = MutableLiveData<LatLng>()
    val lastUserLocation: LiveData<LatLng> = _lastUserLocation

    private val _parkLocation = MutableLiveData<LatLng>(null)
    val parkLocation: LiveData<LatLng?> = _parkLocation

    private val _mapUri = MutableLiveData<Uri>()
    val mapUri: LiveData<Uri> = _mapUri

    init {
        val savedParkLocation = sharedPreferencesDAO.getLocationData(PARK_LOCATION_KEY)
        _parkLocation.value = savedParkLocation
    }

    @SuppressLint("MissingPermission")
    fun getUserLocation() {
        try {
            if (locationPermission.value!!) {
                _lastUserLocation.value = locationRepository.getCurrentUserLocation()
            } else {
                _lastUserLocation.value = null
                _locationPermission.value = false

            }
        } catch (e: SecurityException) {
            Log.e("", "Location error occured", e)
        }
    }


    private fun getUserLocationAndPark() {
        try {
            if (locationPermission.value!!) {
                val newLastLocation = locationRepository.getCurrentUserLocation()

                if ( newLastLocation != null){
                    _lastUserLocation.value = newLastLocation
                    if (_parkLocation.value == null) {
                        _parkLocation.value = lastUserLocation.value

                        if (_parkLocation.value != null)
                            sharedPreferencesDAO.saveLocationData(
                                PARK_LOCATION_KEY,
                                _parkLocation.value!!
                            )
                    }
                } //TODO: Nested if statements to refactor
            } else {
                _lastUserLocation.value = null
                _locationPermission.value = false

            }
        } catch (e: SecurityException) {
            Log.e("", "Location error occured", e)
        }
    }

    fun parkHere() {
        getUserLocationAndPark()
    }

    fun endParking() {
        _parkLocation.value = null
        sharedPreferencesDAO.deleteLocationData(PARK_LOCATION_KEY)
    }

    fun locationPermissionGranted() {
        _locationPermission.value = true
    }

    fun leadToParkLocation() {
        val coords = parkLocation.value

        /**d for driving (default)
        b for bicycling
        l for two-wheeler
        w for walking*/
        val navigationMode = "w"
        if (coords != null)
            _mapUri.value =
                Uri.parse("google.navigation:q=${coords!!.latitude},${coords!!.longitude}&mode=$navigationMode")
    }

}