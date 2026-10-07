package com.example.movilab7.characters

import com.example.movilab7.PersonajeDatos

data class CharactersUiState(
    val isLoading: Boolean = false,
    val data: List<PersonajeDatos> = emptyList(),
    val hasError: Boolean = false
)

data class CharacterUiState(
    val isLoading: Boolean = false,
    val data: PersonajeDatos,
    val hasError: Boolean = false
)