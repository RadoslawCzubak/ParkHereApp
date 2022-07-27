package com.rczubak.parkhereapp.data.repository

import com.rczubak.parkhereapp.data.database.room.ParkingDao
import com.rczubak.parkhereapp.data.database.room.entity.CarRoomDb
import com.rczubak.parkhereapp.data.database.room.entity.ParkingSpotRoomDb
import com.rczubak.parkhereapp.domain.ParkingRepository
import com.rczubak.parkhereapp.domain.model.Car
import com.rczubak.parkhereapp.domain.model.ParkingSpot

class ParkingRepositoryImpl(
    private val parkingDao: ParkingDao
) : ParkingRepository {
    override suspend fun startParking(parkingSpot: ParkingSpot) {
        parkingDao.addNewParkingSpot(ParkingSpotRoomDb(parkingSpot))
    }

    override suspend fun getLastActiveParkingSpot(): ParkingSpot? {
        return parkingDao
            .getLastActiveParkingSpot()?.toParkingSpot()
    }

    override suspend fun getParkingSpots(): List<ParkingSpot> {
        return parkingDao
            .getAllParkingSpotsByIsParked(true)
            .map {
                it.toParkingSpot()
            }
    }

    override suspend fun endParking(parkingSpot: ParkingSpot) {
        val updatedParkingSpot = parkingSpot.copy(isParked = false)
        parkingDao.updateParkingSpot(ParkingSpotRoomDb(updatedParkingSpot))
    }

    override suspend fun addCar(newCar: Car) {
        parkingDao.addNewCar(CarRoomDb(newCar))
    }

    override suspend fun getCars(): List<Car> {
        return parkingDao.getCars()
            .map { it.toCar() }
    }
}