package com.rczubak.parkhereapp.utils

import android.location.Location
import com.google.android.gms.maps.model.LatLng

fun locationToLatLng(location: Location?): LatLng? {
    return if (location != null) {
        LatLng(location.latitude, location.longitude)
    } else {
        null
    }
}