package com.rczubak.parkhereapp.domain.usecase

import com.rczubak.parkhereapp.domain.ParkingRepository
import com.rczubak.parkhereapp.domain.model.Car
import com.rczubak.parkhereapp.domain.model.Coordinates
import com.rczubak.parkhereapp.domain.model.ParkingSpot

class SaveParkingSpotUseCase(
    private val parkingRepository: ParkingRepository
) {
    suspend operator fun invoke(location: Coordinates) {
        parkingRepository.startParking(
            ParkingSpot(
                id = System.currentTimeMillis(),
                isParked = true,
                car = Car.DEFAULT,
                coordinates = location,
                photoUri = null,
                timestamp = System.currentTimeMillis()
            )
        )
    }
}