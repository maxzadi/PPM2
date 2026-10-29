package com.example.movilab7.locations.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.movilab7.core.room.LocationDao
import com.example.movilab7.core.room.LocationEntity
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

data class LocationDetailUiState(
    val isLoading: Boolean = true,
    val data: LocationEntity? = null,
    val hasError: Boolean = false
)

class LocationDetailViewModel(
    private val locationDao: LocationDao,
    private val id: Int
) : ViewModel() {

    private val _uiState = MutableStateFlow(LocationDetailUiState())
    val uiState = _uiState.asStateFlow()

    private var loadJob: Job? = null

    init {
        loadLocation()
    }

    fun loadLocation() {
        loadJob?.cancel()

        loadJob = viewModelScope.launch {
            _uiState.value = LocationDetailUiState(isLoading = true)

            try {
                locationDao.getLocationEntityById(id).collect { location ->
                    _uiState.value = LocationDetailUiState(
                        isLoading = false,
                        data = location
                    )
                }
            } catch (exception: CancellationException) {
                throw exception
            } catch (exception: Exception) {
                Log.e(
                    "LocationDetailVM",
                    "Error al consultar la ubicación",
                    exception
                )

                _uiState.value = LocationDetailUiState(
                    isLoading = false,
                    hasError = true
                )
            }
        }
    }

    fun simulateError() {
        loadJob?.cancel()

        _uiState.value = LocationDetailUiState(
            isLoading = false,
            hasError = true
        )
    }
}

class LocationDetailViewModelFactory(
    private val locationDao: LocationDao,
    private val id: Int
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LocationDetailViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LocationDetailViewModel(locationDao, id) as T
        }

        throw IllegalArgumentException("ViewModel desconocido")
    }
}