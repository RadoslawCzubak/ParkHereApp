package com.rczubak.parkhereapp.main

import android.annotation.SuppressLint
import android.location.Location
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.location.FusedLocationProviderClient

class MainViewModel(val locationProviderClient: FusedLocationProviderClient) : ViewModel() {

    private val _locationPermission = MutableLiveData<Boolean>(false)
    val locationPermission: LiveData<Boolean> = _locationPermission

    private val _lastUserLocation = MutableLiveData<Location>()
    val lastUserLocation: LiveData<Location> = _lastUserLocation

    private val _parkLocation = MutableLiveData<Location>()
    val parkLocation: LiveData<Location?> = _parkLocation

    @SuppressLint("MissingPermission")
    fun getUserLocation() {
        try {
            if (locationPermission.value!!) {
                locationProviderClient.lastLocation
                    .addOnSuccessListener { location ->
                        _lastUserLocation.value = location
                    }
            } else {
                _lastUserLocation.value = null
                _locationPermission.value = false

            }
        } catch (e: SecurityException){
            Log.e("", "Location error occured", e)
        }
    }

    fun parkHere(){
        getUserLocation()
        Log.d("clicked", "clicked")
        if(_parkLocation.value == null)
        _parkLocation.value = lastUserLocation.value
    }

    fun endParking(){
        Log.d("clicked", "clicked")
        _parkLocation.value = null
    }

    fun locationPermissionGranted(){
        _locationPermission.value = true
    }

}