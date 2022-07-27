package com.rczubak.parkhereapp.data.database.room

import androidx.room.*
import com.rczubak.parkhereapp.data.database.room.entity.CarRoomDb
import com.rczubak.parkhereapp.data.database.room.entity.ParkingSpotRoomDb

@Dao
interface ParkingDao {
    @Query("SELECT * FROM parking_spot")
    suspend fun getAllParkingSpots(): List<ParkingSpotRoomDb>

    @Query("SELECT * FROM parking_spot WHERE isParked IS :isParked")
    suspend fun getAllParkingSpotsByIsParked(isParked: Boolean): List<ParkingSpotRoomDb>

    @Query("SELECT * FROM parking_spot WHERE isParked IS :isParked ORDER BY timestamp DESC LIMIT 1")
    suspend fun getLastActiveParkingSpot(isParked: Boolean = true): ParkingSpotRoomDb?

    @Insert
    suspend fun addNewParkingSpot(newParkingSpotRoomDb: ParkingSpotRoomDb)

    @Update
    suspend fun updateParkingSpot(vararg parkingSpot: ParkingSpotRoomDb)

    @Query("SELECT * FROM car")
    suspend fun getCars(): List<CarRoomDb>

    @Insert
    suspend fun addNewCar(car: CarRoomDb)
}

@Database(
    entities = [ParkingSpotRoomDb::class, CarRoomDb::class],
    version = 1
)
abstract class ParkingDatabase : RoomDatabase() {
    abstract fun parkingDao(): ParkingDao
}