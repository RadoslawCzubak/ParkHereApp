package com.rczubak.parkhereapp.features.main

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.rczubak.parkhereapp.MainActivity
import com.rczubak.parkhereapp.R
import com.rczubak.parkhereapp.data.SharedPreferencesDAO
import com.rczubak.parkhereapp.databinding.FragmentMapBinding
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class MapFragment : Fragment() {

    private lateinit var binding: FragmentMapBinding
    private val viewModel: MapViewModel by viewModel()
    private val sharedPreferencesDAO: SharedPreferencesDAO by inject()
    private var map: GoogleMap? = null
    private var marker: Marker? = null

    companion object {
        const val FINE_LOCATION_PERMISSION_REQUEST = 101
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_map, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewModel()
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
                    Timber.i("Location permission granted.")
                    viewModel.locationPermissionGranted()
                    Toast.makeText(context, "Permission granted!", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    Timber.i("Location permission denied.")
                    Toast.makeText(
                        context,
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
        binding.viewModel = viewModel
    }

    private fun setObservers() {
        viewModel.parkLocation.observe(this) { location ->
            if (location == null) {
                removeMarker()
                binding.buttonPark.enabled = true
                binding.buttonRemove.enabled = false
            } else {
                binding.buttonPark.enabled = false
                binding.buttonRemove.enabled = true
                binding.buttonLead.enabled = true
                updateParkingMarker(location)
            }

        }

        viewModel.lastUserLocation.observe(this) { location ->
            updateMapView(location)
            updateMapUI(true)
        }

        viewModel.locationPermission.observe(this) {
            if (it) {
                setMapUI()
            }

            updateMapUI(it)
        }

        viewModel.mapUri.observe(this) { uri ->
            leadToParkLocation(uri)
        }
    }

    private fun getLocationPermission() {
        Timber.i("Checking location permission.")
        if (ContextCompat.checkSelfPermission(
                context!!,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            Timber.i("Location permission already granted.")
            viewModel.locationPermissionGranted()
            getUserLocation()
        } else {
            Timber.i("Trying to get location permission.")
//            ContextCompat.requestPermissions(
//                this,
//                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
//                MainActivity.FINE_LOCATION_PERMISSION_REQUEST
//            )
        }
    }

    private fun getMap() {
        Timber.i("Getting Google maps.")
        val mapFragment =
            childFragmentManager.findFragmentById(R.id.map_fragment) as SupportMapFragment
        mapFragment.getMapAsync {
            Timber.i("Maps get.")
            map = it
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
        Timber.i("Getting user's location")
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

    private fun leadToParkLocation(parkLocationUri: Uri) {
        val mapIntent = Intent(Intent.ACTION_VIEW, parkLocationUri)
        mapIntent.`package` = "com.google.android.apps.maps"
        startActivity(mapIntent)
    }

    private fun removeMarker() {
        if (marker != null) marker!!.remove()
        marker = null
    }
}