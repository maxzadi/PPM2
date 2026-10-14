package com.example.movilab7

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ProfileData(
    val nombre: String = "",
    val guardado: Boolean = false,
    val cierreSesionExitoso: Boolean = false
)

data class ProfileUiState(
    val isLoading: Boolean = true,
    val data: ProfileData = ProfileData(),
    val hasError: Boolean = false
)

class PerfilViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val context =
        application.applicationContext

    private val _uiState = MutableStateFlow(
        ProfileUiState()
    )

    val uiState: StateFlow<ProfileUiState> =
        _uiState.asStateFlow()

    init {
        cargarPerfil()
    }

    fun cargarPerfil() {
        viewModelScope.launch {
            _uiState.value = ProfileUiState(
                isLoading = true,
                data = ProfileData(),
                hasError = false
            )

            try {
                val userData =
                    context.getUserData()

                _uiState.value = ProfileUiState(
                    isLoading = false,
                    data = ProfileData(
                        nombre = userData.nombre,
                        guardado = userData.sesionIniciada,
                        cierreSesionExitoso = false
                    ),
                    hasError = false
                )
            } catch (exception: Exception) {
                _uiState.value = ProfileUiState(
                    isLoading = false,
                    data = ProfileData(),
                    hasError = true
                )
            }
        }
    }

    fun cerrarSesion() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                hasError = false
            )

            try {
                context.clearUserData()

                _uiState.value = ProfileUiState(
                    isLoading = false,
                    data = ProfileData(
                        nombre = "",
                        guardado = false,
                        cierreSesionExitoso = true
                    ),
                    hasError = false
                )
            } catch (exception: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    hasError = true
                )
            }
        }
    }
}