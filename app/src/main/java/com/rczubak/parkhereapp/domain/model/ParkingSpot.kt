package com.rczubak.parkhereapp.domain.model

data class ParkingSpot(
    val id: Long,
    val coordinates: Coordinates,
    val car: Car,
    val timestamp: Long,
    val isParked: Boolean,
    val photoUri: String?
)