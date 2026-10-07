package com.example.movilab7.locations

import LocationDb
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.example.movilab7.LocationID
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

data class LocationDetailUiState(
    val isLoading: Boolean = true,
    val data: Location? = null,
    val hasError: Boolean = false
)

class LocationDetailViewModel(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val route =
        savedStateHandle.toRoute<LocationID>()

    private var loadJob: Job? = null

    private val locationId: Int = route.id

    private val locationDb = LocationDb()

    private val _uiState = MutableStateFlow(
        LocationDetailUiState()
    )

    val uiState: StateFlow<LocationDetailUiState> =
        _uiState.asStateFlow()

    init {
        loadLocation()
    }

    fun loadLocation() {
        loadJob = viewModelScope.launch {
            _uiState.value = LocationDetailUiState(
                isLoading = true,
                data = null,
                hasError = false
            )

            delay(2000.milliseconds)

            try {
                val location =
                    locationDb.getLocationById(locationId)

                if (location == null) {
                    _uiState.value =
                        LocationDetailUiState(
                            isLoading = false,
                            data = null,
                            hasError = true
                        )
                } else {
                    _uiState.value =
                        LocationDetailUiState(
                            isLoading = false,
                            data = location,
                            hasError = false
                        )
                }
            } catch (exception: Exception) {
                _uiState.value =
                    LocationDetailUiState(
                        isLoading = false,
                        data = null,
                        hasError = true
                    )
            }
        }
    }

    fun simulateError() {
        loadJob?.cancel()

        _uiState.value = LocationDetailUiState(
            isLoading = false,
            data = null,
            hasError = true
        )
    }
}