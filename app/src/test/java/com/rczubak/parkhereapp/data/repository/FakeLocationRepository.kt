package com.rczubak.parkhereapp.data.repository

import com.google.android.gms.maps.model.LatLng

class FakeLocationRepository(val latLng: LatLng?): LocationSource {
    override fun getCurrentUserLocation(): LatLng? {
        return latLng
    }

}