package com.rczubak.parkhereapp.data.repository

import android.annotation.SuppressLint
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.model.LatLng
import com.rczubak.parkhereapp.domain.LocationRepository
import com.rczubak.parkhereapp.utils.locationToLatLng

class LocationRepositoryImpl(private val locationProviderClient: FusedLocationProviderClient) :
    LocationRepository {
    @SuppressLint("MissingPermission")
    override fun getCurrentUserLocation(onSuccess: (LatLng?) -> Unit) {
        locationProviderClient.lastLocation.addOnSuccessListener {
            val location = locationToLatLng(it)
            onSuccess(location)
        }
    }
}