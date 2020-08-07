package com.rczubak.parkhereapp.ui.main

import android.annotation.SuppressLint
import android.location.Location
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.model.LatLng
import com.rczubak.parkhereapp.data.SharedPreferencesDAO

class MainViewModel(
    val locationProviderClient: FusedLocationProviderClient,
    val sharedPreferencesDAO: SharedPreferencesDAO
) : ViewModel() {

    private val _locationPermission = MutableLiveData<Boolean>(false)
    val locationPermission: LiveData<Boolean> = _locationPermission

    private val _lastUserLocation = MutableLiveData<LatLng>()
    val lastUserLocation: LiveData<LatLng> = _lastUserLocation

    private val _parkLocation = MutableLiveData<LatLng>(null)
    val parkLocation: LiveData<LatLng?> = _parkLocation

    init {
        val savedParkLocation = sharedPreferencesDAO.getLocationData("parkLocation")
        _parkLocation.value = savedParkLocation
    }

    @SuppressLint("MissingPermission")
    fun getUserLocation() {
        try {
            if (locationPermission.value!!) {
                locationProviderClient.lastLocation
                    .addOnSuccessListener { location ->
                        _lastUserLocation.value = LatLng(location.latitude, location.longitude)
                    }
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
                locationProviderClient.lastLocation
                    .addOnSuccessListener { location ->
                        _lastUserLocation.value = LatLng(location.latitude, location.longitude)
                        if (_parkLocation.value == null) {
                            _parkLocation.value = lastUserLocation.value

                            if(_parkLocation.value != null)
                            sharedPreferencesDAO.saveLocationData(
                                "parkLocation",
                                _parkLocation.value!!
                            )
                        }
                    }
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
        sharedPreferencesDAO.deleteLocationData("parkLocation")
    }

    fun locationPermissionGranted() {
        _locationPermission.value = true
    }

}