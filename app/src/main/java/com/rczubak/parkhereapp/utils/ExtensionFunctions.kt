package com.rczubak.parkhereapp.utils

import com.google.android.gms.maps.model.LatLng
import com.rczubak.parkhereapp.domain.model.Coordinates

fun latLngToCoordinates(coords: Coordinates): LatLng {
    return LatLng(coords.lat, coords.lng)
}