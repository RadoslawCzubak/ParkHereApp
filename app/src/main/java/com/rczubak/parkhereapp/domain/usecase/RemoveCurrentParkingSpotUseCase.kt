package com.rczubak.parkhereapp.domain.usecase

import com.rczubak.parkhereapp.domain.ParkingRepository
import com.rczubak.parkhereapp.domain.model.ParkingSpot

class RemoveCurrentParkingSpotUseCase(
    private val parkingRepository: ParkingRepository
) {
    suspend operator fun invoke(parkingSpot: ParkingSpot) {
        parkingRepository.endParking(parkingSpot)
    }
}