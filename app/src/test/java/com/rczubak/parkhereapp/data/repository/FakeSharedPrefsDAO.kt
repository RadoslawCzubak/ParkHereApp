package com.rczubak.parkhereapp.data.repository

import com.google.android.gms.maps.model.LatLng
import com.rczubak.parkhereapp.data.database.sharedPrefs.SharedPreferencesDAOSource

class FakeSharedPrefsDAO: SharedPreferencesDAOSource {
    override fun getLocationData(key: String): LatLng? {
        return LatLng(12.0, 34.0)
    }

    override fun deleteLocationData(key: String) {
        TODO("Not yet implemented")
    }

    override fun saveLocationData(key: String, data: LatLng) {
        TODO("Not yet implemented")
    }
}