package com.example.movilab7.locations.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movilab7.locations.data.LocationDb
import com.example.movilab7.locations.data.LocationsUiState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LocationsViewModel : ViewModel() {

    private val locationDb = LocationDb()

    private val _uiState = MutableStateFlow(
        LocationsUiState()
    )

    val uiState: StateFlow<LocationsUiState> =
        _uiState.asStateFlow()

    private var loadJob: Job? = null

    init {
        loadLocations()
    }

    fun loadLocations() {
        loadJob?.cancel()

        loadJob = viewModelScope.launch {
            _uiState.value = LocationsUiState(
                isLoading = true,
                data = emptyList(),
                hasError = false
            )

            delay(3000)

            try {
                val locations =
                    locationDb.getAllLocations()

                _uiState.value = LocationsUiState(
                    isLoading = false,
                    data = locations,
                    hasError = false
                )
            } catch (exception: Exception) {
                _uiState.value = LocationsUiState(
                    isLoading = false,
                    data = emptyList(),
                    hasError = true
                )
            }
        }
    }

    fun simulateError() {
        loadJob?.cancel()

        _uiState.value = LocationsUiState(
            isLoading = false,
            data = emptyList(),
            hasError = true
        )
    }
}