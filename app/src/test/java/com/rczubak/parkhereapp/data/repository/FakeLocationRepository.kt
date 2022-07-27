package com.rczubak.parkhereapp.data.repository

import com.google.android.gms.maps.model.LatLng
import com.rczubak.parkhereapp.domain.LocationRepository

class FakeLocationRepository(val latLng: LatLng?): LocationRepository {
    override fun getCurrentUserLocation(onSuccess: (LatLng?) -> Unit) {
        onSuccess(latLng)
    }


}