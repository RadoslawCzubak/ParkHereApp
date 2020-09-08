package com.rczubak.parkhereapp.data.repository

import android.annotation.SuppressLint
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.model.LatLng
import com.rczubak.parkhereapp.utils.locationToLatLng

class LocationRepository(private val locationProviderClient: FusedLocationProviderClient) :
    LocationSource {
    @SuppressLint("MissingPermission")
    override fun getCurrentUserLocation(): LatLng? {
        var location: LatLng? = null
        locationProviderClient.lastLocation.addOnSuccessListener {
            locationToLatLng(it)
        }
        return location
    }
}