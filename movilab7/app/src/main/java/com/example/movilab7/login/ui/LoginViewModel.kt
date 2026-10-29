package com.example.movilab7

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.room.withTransaction
import com.example.movilab7.characters.data.toEntity
import com.example.movilab7.core.AppDatabase
import com.example.movilab7.locations.data.LocationDb
import com.example.movilab7.locations.data.toEntity
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class LoginData(
    val nombre: String = "",
    val guardadoExitoso: Boolean = false
)

data class LoginUiState(
    val isLoading: Boolean = true,
    val data: LoginData = LoginData(),
    val hasError: Boolean = false,
    val errorMessage: String? = null
)

class LoginViewModel(
    application: Application,
    private val database: AppDatabase
) : AndroidViewModel(application) {

    private val context = application.applicationContext

    private val _uiState = MutableStateFlow(LoginUiState())

    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    init {
        getNombre()
    }

    fun getNombre() {
        viewModelScope.launch {
            try {
                val nombre = context.getNombre().orEmpty()

                _uiState.value = LoginUiState(
                    isLoading = false,
                    data = LoginData(nombre = nombre)
                )
            } catch (exception: CancellationException) {
                throw exception
            } catch (exception: Exception) {
                Log.e("LoginViewModel", "Error al leer el nombre", exception)

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        hasError = true,
                        errorMessage = "No se pudo leer el nombre guardado"
                    )
                }
            }
        }
    }

    fun onNombreChange(nuevoNombre: String) {
        if (_uiState.value.isLoading) return

        _uiState.update {
            it.copy(
                data = it.data.copy(
                    nombre = nuevoNombre,
                    guardadoExitoso = false
                ),
                hasError = false,
                errorMessage = null
            )
        }
    }

    fun setNombre() {
        // Evita iniciar dos operaciones por pulsaciones repetidas.
        if (_uiState.value.isLoading ||
            _uiState.value.data.guardadoExitoso
        ) return

        val nombre = _uiState.value.data.nombre.trim()

        if (nombre.isBlank()) {
            _uiState.update {
                it.copy(
                    hasError = true,
                    errorMessage = "Debes escribir tu nombre"
                )
            }
            return
        }

        _uiState.update {
            it.copy(
                isLoading = true,
                hasError = false,
                errorMessage = null
            )
        }

        viewModelScope.launch {
            try {
                if (!context.getSincronizacionCompleta()) {
                    delay(4000)

                    val personajesIniciales = personajes.map {
                        it.toEntity()
                    }

                    val ubicacionesIniciales =
                        LocationDb().getAllLocations().map {
                            it.toEntity()
                        }

                    database.withTransaction {
                        database.characterDao()
                            .insertInitialCharacters(personajesIniciales)

                        database.locationDao()
                            .insertInitialLocationEntitys(ubicacionesIniciales)
                    }
                }

                // Solo activar la sesión después de sincronizar.
                context.completarLogin(nombre)

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        data = it.data.copy(
                            nombre = nombre,
                            guardadoExitoso = true
                        ),
                        hasError = false,
                        errorMessage = null
                    )
                }
            } catch (exception: CancellationException) {
                throw exception
            } catch (exception: Exception) {
                Log.e("LoginViewModel", "Error al iniciar sesión", exception)

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        hasError = true,
                        errorMessage =
                            "No se pudo completar el inicio de sesión. Intenta de nuevo."
                    )
                }
            }
        }
    }
}

class LoginViewModelFactory(
    private val application: Application,
    private val database: AppDatabase
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LoginViewModel(application, database) as T
        }

        throw IllegalArgumentException("ViewModel desconocido")
    }
}