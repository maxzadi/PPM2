package com.example.movilab7

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.withTransaction
import com.example.movilab7.core.AppDatabase
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class LogoutUiState(
    val isLoading: Boolean = false,
    val completado: Boolean = false,
    val error: String? = null
)

class LogoutViewModel(
    context: Context,
    private val database: AppDatabase
) : ViewModel() {

    private val appContext = context.applicationContext

    private val _uiState = MutableStateFlow(LogoutUiState())
    val uiState = _uiState.asStateFlow()

    fun cerrarSesion() {
        if (_uiState.value.isLoading || _uiState.value.completado) return

        _uiState.value = LogoutUiState(isLoading = true)

        viewModelScope.launch {
            try {
                // Invalidar la sesión y la sincronización primero.
                appContext.clearUserData()

                // Borrar ambas tablas juntas.
                database.withTransaction {
                    database.characterDao().deleteAllCharacters()
                    database.locationDao().deleteAllLocationEntitys()
                }

                // La navegación se permite solo al terminar.
                _uiState.value = LogoutUiState(
                    completado = true
                )
            } catch (exception: CancellationException) {
                throw exception
            } catch (exception: Exception) {
                Log.e("CerrarSesionVM", "Error al cerrar sesión", exception)

                _uiState.value = LogoutUiState(
                    error = "No se pudo completar el cierre de sesión. Reintenta."
                )
            }
        }
    }
}