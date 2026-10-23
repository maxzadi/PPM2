package com.example.movilab7.locations.data

import com.example.movilab7.core.room.LocationEntity
data class LocationsUiState(
    val isLoading: Boolean = true,
    val data: List<LocationEntity> = emptyList(),
    val hasError: Boolean = false
)