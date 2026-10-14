package com.example.movilab7

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first

val Context.dataStore: DataStore<Preferences> by
preferencesDataStore(name = "settings")

private val nombreKey =
    stringPreferencesKey("nombre")

private val sesionIniciadaKey =
    booleanPreferencesKey("sesion_iniciada")

data class UserData(
    val nombre: String = "",
    val sesionIniciada: Boolean = false
)

suspend fun Context.setUserData(
    nombre: String
) {
    dataStore.edit { preferences ->
        preferences[nombreKey] = nombre
        preferences[sesionIniciadaKey] = true
    }
}

suspend fun Context.getUserData(): UserData {
    val preferences =
        dataStore.data.first()

    return UserData(
        nombre = preferences[nombreKey] ?: "",
        sesionIniciada =
            preferences[sesionIniciadaKey] ?: false
    )
}

suspend fun Context.getNombre(): String?{
    val preferences =
        dataStore.data.first()

    return preferences[nombreKey]
}

suspend fun Context.clearUserData() {
    dataStore.edit { preferences ->
        preferences.remove(nombreKey)
        preferences[sesionIniciadaKey] = false
    }
}
suspend fun Context.getSesion(): Boolean {
    val preferences =
        dataStore.data.first()

    return preferences[sesionIniciadaKey] ?: false
}