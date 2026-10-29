package com.example.movilab7.characters

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.movilab7.core.room.Character
import com.example.movilab7.core.room.CharacterDao
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

data class CharacterDetailUiState(
    val isLoading: Boolean = true,
    val data: Character? = null,
    val hasError: Boolean = false
)

class CharacterDetailViewModel(
    private val characterDao: CharacterDao,
    private val id: Int
) : ViewModel() {

    private val _uiState = MutableStateFlow(CharacterDetailUiState())
    val uiState = _uiState.asStateFlow()

    private var loadJob: Job? = null

    init {
        loadCharacter()
    }

    fun loadCharacter() {
        loadJob?.cancel()

        loadJob = viewModelScope.launch {
            _uiState.value = CharacterDetailUiState(isLoading = true)

            try {
                characterDao.getCharacterById(id).collect { personaje ->
                    _uiState.value = CharacterDetailUiState(
                        isLoading = false,
                        data = personaje
                    )
                }
            } catch (exception: CancellationException) {
                throw exception
            } catch (exception: Exception) {
                Log.e(
                    "CharacterDetailVM",
                    "Error al consultar el personaje",
                    exception
                )

                _uiState.value = CharacterDetailUiState(
                    isLoading = false,
                    hasError = true
                )
            }
        }
    }

    fun simulateError() {
        loadJob?.cancel()

        _uiState.value = CharacterDetailUiState(
            isLoading = false,
            hasError = true
        )
    }

    fun cambiarFavorito() {
        val personaje = _uiState.value.data ?: return

        viewModelScope.launch {
            try {
                characterDao.updateFavorito(
                    id = personaje.id,
                    favorito = !personaje.favorito
                )
            } catch (exception: CancellationException) {
                throw exception
            } catch (exception: Exception) {
                Log.e("CharacterDetailVM", "Error al guardar favorito", exception)

                _uiState.value = _uiState.value.copy(
                    hasError = true
                )
            }
        }
    }
}

class CharacterDetailViewModelFactory(
    private val characterDao: CharacterDao,
    private val id: Int
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CharacterDetailViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CharacterDetailViewModel(characterDao, id) as T
        }

        throw IllegalArgumentException("ViewModel desconocido")
    }
}