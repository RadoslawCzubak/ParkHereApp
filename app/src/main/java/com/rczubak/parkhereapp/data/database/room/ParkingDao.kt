package com.rczubak.parkhereapp.data.database.room

import androidx.room.*
import com.rczubak.parkhereapp.data.database.room.entity.CarRoomDb
import com.rczubak.parkhereapp.data.database.room.entity.CoordinatesRoomDb
import com.rczubak.parkhereapp.data.database.room.entity.ParkingSpotRoomDb

@Dao
interface ParkingDao {
    @Query("SELECT * FROM parking_spot")
    fun getAllParkingSpots(): List<ParkingSpotRoomDb>

    @Query("SELECT * FROM parking_spot WHERE isParked IS :isParked")
    fun getAllParkingSpotsByIsParked(isParked: Boolean): List<ParkingSpotRoomDb>

    @Insert
    fun addNewParkingSpot(newParkingSpotRoomDb: ParkingSpotRoomDb)

    @Update
    fun updateParkingSpot(vararg parkingSpot: ParkingSpotRoomDb)

    @Query("SELECT * FROM car")
    fun getCars(): List<CarRoomDb>

    @Insert
    fun addNewCar(car: CarRoomDb)
}

@Database(
    entities = [ParkingSpotRoomDb::class, CarRoomDb::class, CoordinatesRoomDb::class],
    version = 1
)
abstract class ParkingDatabase : RoomDatabase() {
    abstract fun parkingDao(): ParkingDao
}