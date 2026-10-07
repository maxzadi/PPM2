package com.example.movilab7.characters

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.example.movilab7.PersonajeDatos
import com.example.movilab7.PersonajeID
import com.example.movilab7.personajes
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

data class CharacterDetailUiState(
    val isLoading: Boolean = true,
    val data: PersonajeDatos? = null,
    val hasError: Boolean = false
)

class CharacterDetailViewModel(
    savedStateHandle: SavedStateHandle
) : ViewModel() {


    private var loadJob: Job? = null

    private val route =
        savedStateHandle.toRoute<PersonajeID>()

    private val characterId: Int = route.id

    private val _uiState = MutableStateFlow(
        CharacterDetailUiState()
    )

    val uiState: StateFlow<CharacterDetailUiState> =
        _uiState.asStateFlow()

    init {
        loadCharacter()
    }

    fun loadCharacter() {
        loadJob = viewModelScope.launch {
            _uiState.value = CharacterDetailUiState(
                isLoading = true,
                data = null,
                hasError = false
            )

            delay(2000.milliseconds)

            try {
                val character = personajes.firstOrNull {
                    it.id == characterId
                }

                if (character == null) {
                    _uiState.value = CharacterDetailUiState(
                        isLoading = false,
                        data = null,
                        hasError = true
                    )
                } else {
                    _uiState.value = CharacterDetailUiState(
                        isLoading = false,
                        data = character,
                        hasError = false
                    )
                }
            } catch (exception: Exception) {
                _uiState.value = CharacterDetailUiState(
                    isLoading = false,
                    data = null,
                    hasError = true
                )
            }
        }
    }

    fun simulateError() {
        loadJob?.cancel()

        _uiState.value = CharacterDetailUiState(
            isLoading = false,
            data = null,
            hasError = true
        )
    }
}