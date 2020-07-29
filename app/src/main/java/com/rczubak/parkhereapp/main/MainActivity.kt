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
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
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
import com.rczubak.parkhereapp.vmFactory.ViewModelFactory
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val TAG = MainActivity::class.java.simpleName
    private lateinit var viewModel: MainViewModel
    private var map: GoogleMap? = null
    private val markers = ArrayList<Marker>()

    companion object {
        const val FINE_LOCATION_PERMISSION_REQUEST = 101
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupViewModel()
        getLocationPermission()
        getMap()
        setListeners()
        setObservers()
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
                    viewModel.locationPermissionGranted()
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

    private fun setupViewModel() {
        val factory =
            ViewModelFactory(LocationServices.getFusedLocationProviderClient(applicationContext))
        viewModel = ViewModelProvider(this, factory).get(MainViewModel::class.java)
    }

    private fun setObservers() {
        viewModel.parkLocation.observe(this, Observer {location ->
            updateParkingMarker(location)
        })

        viewModel.lastUserLocation.observe(this, Observer {location ->
            updateMapView(location)
            updateMapUI(true)
        })

        viewModel.locationPermission.observe(this, Observer {
            if (it){
                setMapUI()
            }

            updateMapUI(it)
        })
    }

    private fun getLocationPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            viewModel.locationPermissionGranted()
            getUserLocation()
        } else {
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
        }
    }

    private fun setMapUI() {
        map?.uiSettings?.isCompassEnabled = true
    }


    @SuppressLint("MissingPermission")
    private fun updateMapUI(isLocationAllowed: Boolean) {

        if (map == null) return

        if (isLocationAllowed){
            map?.isMyLocationEnabled = true
            map?.uiSettings?.isMyLocationButtonEnabled = true
        } else {
            map?.isMyLocationEnabled = false
            map?.uiSettings?.isMyLocationButtonEnabled = false
        }
    }

    private fun getUserLocation() {
        viewModel.getUserLocation()
    }

    private fun updateMapView(lastUserLocation: Location?) {
        if (lastUserLocation != null) {
            map?.moveCamera(
                CameraUpdateFactory.newLatLngZoom(
                    LatLng(
                        lastUserLocation.latitude,
                        lastUserLocation.longitude
                    ), 13f
                )
            )
        }
    }

    private fun updateParkingMarker(parkLocation: Location?) {
        if (parkLocation != null) {
            val marker = map?.addMarker(
                MarkerOptions().position(
                    LatLng(
                        parkLocation!!.latitude,
                        parkLocation!!.longitude
                    )
                ).title("Parked Here!")
            )
            if (marker != null) {
                markers.add(marker)
            }
        }
    }

    private fun setListeners() {
        button_park.setOnClickListener {
            getUserLocation()
            viewModel.parkHere()
        }

        button_remove.setOnClickListener {
            for (marker in markers) {
                marker.remove()
            }
        }
    }
}