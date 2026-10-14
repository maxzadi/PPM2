package com.example.movilab7

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class LoginData(
    val nombre: String = "",
    val guardadoExitoso: Boolean = false
)

data class LoginUiState(
    val isLoading: Boolean = true,
    val data: LoginData = LoginData(),
    val hasError: Boolean = false
)

class LoginViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val context =
        application.applicationContext

    private val _uiState = MutableStateFlow(
        LoginUiState()
    )

    val uiState: StateFlow<LoginUiState> =
        _uiState.asStateFlow()

    init {
        getNombre()
    }

    fun getNombre() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                hasError = false
            )

            try {
                val nombreGuardado =
                    context.getNombre()

                _uiState.value = LoginUiState(
                    isLoading = false,
                    data = LoginData(
                        nombre = nombreGuardado!!,
                        guardadoExitoso = false
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

    fun onNombreChange(nuevoNombre: String) {
        _uiState.value = _uiState.value.copy(
            data = _uiState.value.data.copy(
                nombre = nuevoNombre,
                guardadoExitoso = false
            ),
            hasError = false
        )
    }

    fun setNombre() {
        val nombre =
            _uiState.value.data.nombre.trim()

        if (nombre.isBlank()) {
            _uiState.value = _uiState.value.copy(
                hasError = true
            )

            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                hasError = false
            )

            try {
                context.setUserData(nombre)

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    data = _uiState.value.data.copy(
                        nombre = nombre,
                        guardadoExitoso = true
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