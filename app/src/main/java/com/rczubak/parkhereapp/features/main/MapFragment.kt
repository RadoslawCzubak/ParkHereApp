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
import androidx.activity.result.contract.ActivityResultContracts
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
import com.rczubak.parkhereapp.R
import com.rczubak.parkhereapp.databinding.FragmentMapBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class MapFragment : Fragment() {

    private lateinit var binding: FragmentMapBinding
    private val viewModel: MapViewModel by viewModel()
    private var map: GoogleMap? = null
    private var marker: Marker? = null

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            setMapUI()
        }
        updateMapUI(isGranted)
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
        getMap()
    }

    private fun setupViewModel() {
        binding.viewModel = viewModel
    }

    private fun setObservers() {
        viewModel.parkLocation.observe(viewLifecycleOwner) { location ->
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

        viewModel.lastUserLocation.observe(viewLifecycleOwner) { location ->
            updateMapView(location)
            updateMapUI(true)
        }

        viewModel.mapUri.observe(viewLifecycleOwner) { uri ->
            leadToParkLocation(uri)
        }
    }

    private fun getLocationPermission() {
        Timber.i("Checking location permission.")
        context?.let {
            when {
                ContextCompat.checkSelfPermission(
                    it,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED -> {
                    setMapUI()
                    updateMapUI(true)
                    getUserLocation()
                }
                shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION) -> {
                    Toast.makeText(
                        context,
                        "Location permission is needed to save your park spot!",
                        Toast.LENGTH_LONG
                    ).show()
                }
                else -> {
                    requestPermissionLauncher.launch(
                        Manifest.permission.ACCESS_FINE_LOCATION
                    )
                }
            }
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

    private fun updateMapView(lastUserLocation: LatLng?, zoom: Float = 13f) {
        if (lastUserLocation != null) {
            map?.moveCamera(
                CameraUpdateFactory.newLatLngZoom(
                    lastUserLocation, zoom
                )
            )
        }
    }

    private fun updateParkingMarker(parkLocation: LatLng?) {
        if (marker == null && parkLocation != null) {
            addMarker(parkLocation)
        } else if (marker != null && parkLocation != null) {
            removeMarker()
            updateParkingMarker(parkLocation)
        }
    }

    private fun addMarker(location: LatLng) {
        val marker = map?.addMarker(
            MarkerOptions().position(
                location
            ).title("Parked Here!")
                .icon(BitmapDescriptorFactory.defaultMarker(215f))
        )
        this.marker = marker
    }

    private fun removeMarker() {
        if (marker != null) marker!!.remove()
        marker = null
    }

    private fun leadToParkLocation(parkLocationUri: Uri) {
        val mapIntent = Intent(Intent.ACTION_VIEW, parkLocationUri)
        mapIntent.`package` = "com.google.android.apps.maps"
        startActivity(mapIntent)
    }
}