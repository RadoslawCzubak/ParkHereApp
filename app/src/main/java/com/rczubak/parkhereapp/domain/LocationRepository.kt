package com.rczubak.parkhereapp.domain

import com.google.android.gms.maps.model.LatLng

interface LocationRepository {
    fun getCurrentUserLocation(onSuccess: (LatLng?) -> Unit)
}