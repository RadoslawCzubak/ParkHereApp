package com.rczubak.parkhereapp.domain.usecase

import com.rczubak.parkhereapp.domain.LocationRepository
import com.rczubak.parkhereapp.domain.model.Coordinates
import kotlin.coroutines.suspendCoroutine

class GetCurrentLocationUseCase(
    private val locationRepository: LocationRepository
) {
    suspend operator fun invoke(): Coordinates? {
        return suspendCoroutine { cont ->
            locationRepository.getCurrentUserLocation(
                onSuccess = {
                    if (it == null)
                        cont.resumeWith(Result.success(null))
                    else
                        cont.resumeWith(Result.success(Coordinates(it.latitude, it.longitude)))
                }
            )
        }
    }
}