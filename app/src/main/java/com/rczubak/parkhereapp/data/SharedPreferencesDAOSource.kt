package com.rczubak.parkhereapp.data

import com.google.android.gms.maps.model.LatLng

interface SharedPreferencesDAOSource{
    fun getLocationData(key: String): LatLng?
    fun deleteLocationData(key: String)
    fun saveLocationData(key: String, data: LatLng)
}