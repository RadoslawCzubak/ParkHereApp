package com.rczubak.parkhereapp.domain

interface ParkingRepository {
    fun startParking()
    fun endParking()
    fun addCar()
}