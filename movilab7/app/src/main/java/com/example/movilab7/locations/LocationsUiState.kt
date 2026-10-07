package com.example.movilab7.locations

import com.example.movilab7.locations.Location

data class LocationsUiState(
    val isLoading: Boolean = true,
    val data: List<Location> = emptyList(),
    val hasError: Boolean = false
)