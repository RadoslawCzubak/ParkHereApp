package com.rczubak.parkhereapp.data

import android.app.Application
import android.content.Context
import android.location.Location
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.rczubak.parkhereapp.R

class SharedPreferencesDAO(application: Application) {
    private val sharedPrefs = application.getSharedPreferences(application.applicationContext.getString(R.string.application_sharedprefs_key), Context.MODE_PRIVATE)

    fun getLocationData(key: String): LatLng? {
        val gson = GsonBuilder().create()
        val locationJson = sharedPrefs.getString(key,"")
        when(locationJson){
            "" -> return null
            else -> return gson.fromJson(locationJson, LatLng::class.java)
        }
    }

    fun deleteLocationData(key: String){
        with(sharedPrefs.edit()){
            remove(key)
            commit()
        }
    }

    fun saveLocationData(key: String, data: LatLng){
        val gson = GsonBuilder().create()
        val json = gson.toJson(data)
        with(sharedPrefs.edit()){
            putString(key, json)
            commit()
        }
    }
}