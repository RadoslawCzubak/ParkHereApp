package com.rczubak.parkhereapp.domain

import com.rczubak.parkhereapp.domain.model.Car
import com.rczubak.parkhereapp.domain.model.ParkingSpot

interface ParkingRepository {
    suspend fun startParking(parkingSpot: ParkingSpot)
    suspend fun getLastActiveParkingSpot(): ParkingSpot?
    suspend fun getParkingSpots(): List<ParkingSpot>
    suspend fun endParking(parkingSpot: ParkingSpot)
    suspend fun addCar(newCar: Car)
    suspend fun getCars(): List<Car>
}