package com.example.movilab7.locations.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.movilab7.core.room.LocationEntity
import com.example.movilab7.core.room.LocationDao
import com.example.movilab7.locations.data.LocationDb
import com.example.movilab7.locations.data.LocationsUiState
import com.example.movilab7.locations.data.toEntity
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import android.util.Log

class LocationsViewModelFactory(
    private val locationDao: LocationDao
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LocationsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LocationsViewModel(locationDao) as T
        }

        throw IllegalArgumentException("ViewModel desconocido")
    }
}

class LocationsViewModel(
    private val locationDao: LocationDao
) : ViewModel() {

    private val locationDb = LocationDb()

    private val _uiState = MutableStateFlow(LocationsUiState())

    val uiState: StateFlow<LocationsUiState> =
        _uiState.asStateFlow()

    private var loadJob: Job? = null

    init {
        loadLocations()
    }

    fun loadLocations() {
        loadJob?.cancel()

        loadJob = viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoading = true,
                    hasError = false
                )
            }

            try {
                // Convertir los datos de ejemplo a entidades de Room.
                val locationsIniciales = locationDb.getAllLocations().map {
                    it.toEntity()
                }

                // Guardar únicamente las ubicaciones que no existen.
                locationDao.insertInitialLocationEntitys(locationsIniciales)

                // Mostrar y observar los datos de Room.
                locationDao.getLocationEntitys().collect { locations ->
                    _uiState.value = LocationsUiState(
                        isLoading = false,
                        data = locations,
                        hasError = false
                    )
                }
            } catch (exception: CancellationException) {
                throw exception
            } catch (exception: Exception) {
                Log.e(
                    "LocationsViewModel",
                    "Error al cargar ubicaciones",
                    exception
                )

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        hasError = true
                    )
                }
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