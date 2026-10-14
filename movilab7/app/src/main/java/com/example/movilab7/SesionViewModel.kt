package com.example.movilab7

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class SessionUiState(
    val isLoading: Boolean = true,
    val data: Boolean = false,
    val hasError: Boolean = false
)

class SessionViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val context =
        application.applicationContext

    private val _uiState = MutableStateFlow(
        SessionUiState()
    )

    val uiState: StateFlow<SessionUiState> =
        _uiState.asStateFlow()

    init {
        revisarSesion()
    }

    private fun revisarSesion() {
        viewModelScope.launch {
            try {
                val userData =
                    context.getUserData()

                _uiState.value = SessionUiState(
                    isLoading = false,
                    data = userData.sesionIniciada,
                    hasError = false
                )
            } catch (exception: Exception) {
                _uiState.value = SessionUiState(
                    isLoading = false,
                    data = false,
                    hasError = true
                )
            }
        }
    }
}
