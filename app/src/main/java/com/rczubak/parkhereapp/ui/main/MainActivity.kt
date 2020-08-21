package com.rczubak.parkhereapp.ui.main

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.rczubak.parkhereapp.R
import com.rczubak.parkhereapp.data.SharedPreferencesDAO
import com.rczubak.parkhereapp.databinding.ActivityMainBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private val TAG = MainActivity::class.java.simpleName
    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModel()
    private lateinit var sharedPreferencesDAO: SharedPreferencesDAO
    private var map: GoogleMap? = null
    private var marker: Marker? = null

    companion object {
        const val FINE_LOCATION_PERMISSION_REQUEST = 101
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        getMap()
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
        sharedPreferencesDAO = SharedPreferencesDAO(application)
        binding.viewModel = viewModel
    }

    private fun setObservers() {
        viewModel.parkLocation.observe(this, Observer { location ->
            if (location == null) {
                removeMarker()
                binding.buttonPark.enabled = true
                binding.buttonRemove.enabled = false
            } else {
                binding.buttonPark.enabled = false
                binding.buttonRemove.enabled = true
                updateParkingMarker(location)
            }

        })

        viewModel.lastUserLocation.observe(this, Observer { location ->
            updateMapView(location)
            updateMapUI(true)
        })

        viewModel.locationPermission.observe(this, Observer {
            if (it) {
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
        mapFragment.getMapAsync{
            map=it
            setupViewModel()
            setObservers()
            getLocationPermission()
        }
    }

    private fun setMapUI() {
        map?.uiSettings?.isCompassEnabled = true
    }


    @SuppressLint("MissingPermission")
    private fun updateMapUI(isLocationAllowed: Boolean) {

        if (map == null) return

        if (isLocationAllowed) {
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

    private fun updateMapView(lastUserLocation: LatLng?) {
        if (lastUserLocation != null) {
            map?.moveCamera(
                CameraUpdateFactory.newLatLngZoom(
                    lastUserLocation, 13f
                )
            )
        }
    }

    private fun updateParkingMarker(parkLocation: LatLng?) {
        if (marker == null && parkLocation != null) {
            val marker = map?.addMarker(
                MarkerOptions().position(
                    parkLocation
                ).title("Parked Here!")
                    .icon(BitmapDescriptorFactory.defaultMarker(215f))
            )
            this.marker = marker
        } else if (marker != null && parkLocation != null) {
            removeMarker()
            updateParkingMarker(parkLocation)
        }
    }

    private fun removeMarker() {
        if (marker != null) marker!!.remove()
        marker = null
    }
}