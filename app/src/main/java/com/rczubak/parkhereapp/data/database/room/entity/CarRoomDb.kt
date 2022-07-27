package com.rczubak.parkhereapp.data.database.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.rczubak.parkhereapp.domain.model.Car

@Entity(tableName = "car")
data class CarRoomDb(
    @PrimaryKey val id: Long,
    val name: String,
    val photoUri: String
) {
    constructor(car: Car) : this(
        car.id,
        car.name,
        car.photoUri
    )

    fun toCar(): Car = Car(
        id, name, photoUri
    )
}
