package com.example.movilab7.characters

import com.example.movilab7.core.room.Character
import com.example.movilab7.characters.data.PersonajeDatos

data class CharactersUiState(
    val isLoading: Boolean = false,
    val data: List<Character> = emptyList(),
    val hasError: Boolean = false
)