package com.rczubak.parkhereapp.data.database.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.rczubak.parkhereapp.domain.model.Coordinates

@Entity
data class CoordinatesRoomDb(
    @PrimaryKey val id: Long,
    val lat: Double,
    val lng: Double
) {
    constructor(coordinates: Coordinates) : this(
        coordinates.id,
        coordinates.lat,
        coordinates.lng
    )

    fun toCoordinates(): Coordinates = Coordinates(
        id, lat, lng
    )
}