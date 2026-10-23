package com.example.movilab7.characters.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movilab7.characters.CharactersUiState
import com.example.movilab7.characters.data.toEntity
import com.example.movilab7.core.room.CharacterDao
import com.example.movilab7.personajes
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CharactersViewModel(
    private val characterDao: CharacterDao
) : ViewModel() {

    private var loadJob: Job? = null

    private val _uiState = MutableStateFlow(CharactersUiState())

    val uiState: StateFlow<CharactersUiState> =
        _uiState.asStateFlow()

    init {
        loadCharacters()
    }

    fun loadCharacters() {
        loadJob?.cancel()

        loadJob = viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoading = true,
                    hasError = false
                )
            }

            delay(3000)

            try {
                // Inserta los personajes que todavía no existen.
                characterDao.insertInitialCharacters(
                    personajes.map { it.toEntity() }
                )

                // Observa los datos guardados en Room.
                characterDao.getCharacters().collect { lista ->
                    _uiState.value = CharactersUiState(
                        isLoading = false,
                        data = lista,
                        hasError = false
                    )
                }
            } catch (exception: CancellationException) {
                throw exception
            } catch (exception: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        hasError = true
                    )
                }
            }
        }
    }

    fun cambiarFavorito(id: Int, favorito: Boolean) {
        viewModelScope.launch {
            try {
                characterDao.updateFavorito(id, favorito)
                // El Flow de Room emitirá la lista actualizada.
            } catch (exception: CancellationException) {
                throw exception
            } catch (exception: Exception) {
                _uiState.update {
                    it.copy(hasError = true)
                }
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
}