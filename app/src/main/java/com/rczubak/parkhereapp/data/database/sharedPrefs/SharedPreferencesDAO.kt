package com.rczubak.parkhereapp.data.database.sharedPrefs

import android.content.Context
import com.google.android.gms.maps.model.LatLng
import com.google.gson.GsonBuilder
import com.rczubak.parkhereapp.R

class SharedPreferencesDAO(context: Context) : SharedPreferencesDAOSource {
    private val sharedPrefs = context.getSharedPreferences(context.getString(R.string.application_sharedprefs_key), Context.MODE_PRIVATE)

    override fun getLocationData(key: String): LatLng? {
        val gson = GsonBuilder().create()
        val locationJson = sharedPrefs.getString(key,"")
        when(locationJson){
            "" -> return null
            else -> return gson.fromJson(locationJson, LatLng::class.java)
        }
    }

    override fun deleteLocationData(key: String){
        with(sharedPrefs.edit()){
            remove(key)
            commit()
        }
    }

    override fun saveLocationData(key: String, data: LatLng){
        val gson = GsonBuilder().create()
        val json = gson.toJson(data)
        with(sharedPrefs.edit()){
            putString(key, json)
            commit()
        }
    }
}