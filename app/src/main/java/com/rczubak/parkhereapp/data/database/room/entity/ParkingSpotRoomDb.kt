package com.rczubak.parkhereapp.data.database.room.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.rczubak.parkhereapp.domain.model.ParkingSpot

@Entity(tableName = "parking_spot")
data class ParkingSpotRoomDb(
    @PrimaryKey val id: Long,
    @Embedded val coordinates: CoordinatesRoomDb,
    @Embedded val car: CarRoomDb,
    val timestamp: Long,
    val isParked: Boolean,
    val photoUri: String?
) {
    constructor(parkingSpot: ParkingSpot) : this(
        parkingSpot.id,
        CoordinatesRoomDb(parkingSpot.coordinates),
        CarRoomDb(parkingSpot.car),
        parkingSpot.timestamp,
        parkingSpot.isParked,
        parkingSpot.photoUri
    )

    fun toParkingSpot() =
        ParkingSpot(id, coordinates.toCoordinates(), car.toCar(), timestamp, isParked, photoUri)
}