package com.rczubak.parkhereapp.data.repository

import com.google.android.gms.maps.model.LatLng

interface LocationSource {
    fun getCurrentUserLocation(onSuccess: (LatLng?) -> Unit)
}