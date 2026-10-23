package com.example.movilab7.characters

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.movilab7.characters.ui.CharactersViewModel
import com.example.movilab7.core.room.CharacterDao

class CharactersViewModelFactory(
    private val characterDao: CharacterDao
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CharactersViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CharactersViewModel(characterDao) as T
        }

        throw IllegalArgumentException("ViewModel desconocido")
    }
}