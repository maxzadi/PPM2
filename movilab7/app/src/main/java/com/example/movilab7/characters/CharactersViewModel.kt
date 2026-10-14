package com.example.movilab7.characters

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movilab7.characters.data.PersonajeDatos
import com.example.movilab7.personajes
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

class CharactersViewModel : ViewModel() {

    private var loadJob: Job? = null

    private val _uiState = MutableStateFlow(
        CharactersUiState()
    )

    val uiState: StateFlow<CharactersUiState> =
        _uiState.asStateFlow()

    init {
        loadCharacters()
    }

    fun loadCharacters() {
        loadJob?.cancel()

        loadJob = viewModelScope.launch {
            _uiState.value = CharactersUiState(
                isLoading = true,
                data = emptyList(),
                hasError = false
            )

            delay(3000)

            try {
                _uiState.value = CharactersUiState(
                    isLoading = false,
                    data = personajes,
                    hasError = false
                )
            } catch (exception: Exception) {
                _uiState.value = CharactersUiState(
                    isLoading = false,
                    data = emptyList(),
                    hasError = true
                )
            }
        }
    }

    fun simulateError() {
        loadJob?.cancel()

        _uiState.value = CharactersUiState(
            isLoading = false,
            data = emptyList(),
            hasError = true
        )
    }

    private fun getCharacters(): List<PersonajeDatos> {
        return personajes
    }
}