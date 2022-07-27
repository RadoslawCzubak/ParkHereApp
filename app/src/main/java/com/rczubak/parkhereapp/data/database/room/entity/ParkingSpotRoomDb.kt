package com.rczubak.parkhereapp.data.database.room.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.rczubak.parkhereapp.domain.model.Coordinates
import com.rczubak.parkhereapp.domain.model.ParkingSpot

@Entity(tableName = "parking_spot")
data class ParkingSpotRoomDb(
    @PrimaryKey val id: Long,
    @Embedded(prefix = "car_") val car: CarRoomDb,
    val lat: Double,
    val lng: Double,
    val timestamp: Long,
    val isParked: Boolean,
    val photoUri: String?
) {
    constructor(parkingSpot: ParkingSpot) : this(
        parkingSpot.id,
        CarRoomDb(parkingSpot.car),
        parkingSpot.coordinates.lat,
        parkingSpot.coordinates.lng,
        parkingSpot.timestamp,
        parkingSpot.isParked,
        parkingSpot.photoUri
    )

    fun toParkingSpot() =
        ParkingSpot(id, Coordinates(lat, lng), car.toCar(), timestamp, isParked, photoUri)
}