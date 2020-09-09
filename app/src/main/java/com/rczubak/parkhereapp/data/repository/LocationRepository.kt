package com.rczubak.parkhereapp.data.repository

import android.annotation.SuppressLint
import android.location.Location
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.model.LatLng
import com.rczubak.parkhereapp.utils.locationToLatLng

class LocationRepository(private val locationProviderClient: FusedLocationProviderClient) :
    LocationSource {
    @SuppressLint("MissingPermission")
    override fun getCurrentUserLocation(onSuccess: (LatLng?) -> Unit) {
        locationProviderClient.lastLocation.addOnSuccessListener {
            val location = locationToLatLng(it)
            onSuccess(location)
        }
    }
}