package com.rczubak.parkhereapp.main

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.rczubak.parkhereapp.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val TAG = MainActivity::class.java.simpleName
    private var locationPermission = false
    private var map: GoogleMap? = null
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private var lastUserLocation: Location? = null
    private val markers = ArrayList<Marker>()

    companion object {
        const val FINE_LOCATION_PERMISSION_REQUEST = 101
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setUtils()
        getLocationPermission()
        getMap()
        setListeners()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            FINE_LOCATION_PERMISSION_REQUEST -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    locationPermission = true
                    updateMapUI()
                    Toast.makeText(applicationContext, "Permission granted!", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    Toast.makeText(
                        applicationContext,
                        "Permission denied, we need them to localize your park place!",
                        Toast.LENGTH_LONG
                    ).show()
                }
                return
            }
            else -> {
                return
            } //Do nothing
        }
    }

    fun setUtils(){
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(applicationContext)
    }

    fun getLocationPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            locationPermission = true
        }

        else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                FINE_LOCATION_PERMISSION_REQUEST
            )
        }
    }

    private fun getMap() {
        val mapFragment =
            supportFragmentManager.findFragmentById(R.id.map_fragment) as SupportMapFragment
        mapFragment.getMapAsync {
            map = it
            setMapUI()
            updateMapUI()
        }
    }

    private fun setMapUI(){
        map?.uiSettings?.isCompassEnabled = true
    }


    @SuppressLint("MissingPermission")
    private fun updateMapUI() {

        if (map == null) return

        if (locationPermission) {
            map?.isMyLocationEnabled = true
            map?.uiSettings?.isMyLocationButtonEnabled = true
        }
        else{
            map?.isMyLocationEnabled = false
            map?.uiSettings?.isMyLocationButtonEnabled = false
        }
    }

    private fun getUserLocation(){
        try {
            if(locationPermission){
                val locationResult = fusedLocationProviderClient.lastLocation
                    .addOnSuccessListener {location ->
                        lastUserLocation = location
                        updateMapView()
                    } }
            else{
                lastUserLocation = null
                getLocationPermission()
                getUserLocation()
            }
            } catch (e: SecurityException){
            Log.d(TAG, "ERROR OCCURED", e)
        }
    }

    private fun updateMapView(){
        if (lastUserLocation != null) {
            map?.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(lastUserLocation!!.latitude, lastUserLocation!!.longitude), 13f))
        }
    }

    private fun parkHere(){
        if (lastUserLocation != null){
            val marker = map?.addMarker(
                MarkerOptions().position(LatLng(lastUserLocation!!.latitude, lastUserLocation!!.longitude)).title("Parked Here!")
            )
            if (marker != null){
                markers.add(marker)
            }
        }

    }

    private fun setListeners(){
        button_park.setOnClickListener{
            getUserLocation()
            parkHere()
        }

        button_remove.setOnClickListener {
            for (marker in markers){
                marker.remove()
            }
        }
    }

}