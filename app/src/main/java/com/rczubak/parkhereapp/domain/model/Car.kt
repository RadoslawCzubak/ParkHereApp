package com.rczubak.parkhereapp.domain.model

data class Car(
    val id: Long,
    val name: String,
    val photoUri: String
) {
    companion object {
        val DEFAULT = Car(
            id = 0,
            name = "Unknown",
            photoUri = ""
        )
    }
}
