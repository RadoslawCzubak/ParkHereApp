package com.rczubak.parkhereapp.domain.usecase

import com.rczubak.parkhereapp.domain.ParkingRepository
import com.rczubak.parkhereapp.domain.model.ParkingSpot

class GetLastParkingSpotUseCase(
    private val parkingRepository: ParkingRepository
) {
    suspend operator fun invoke(): ParkingSpot? {
        return parkingRepository.getLastActiveParkingSpot()
    }
}