package com.example.movilab7.characters.data

import com.example.movilab7.core.room.Character

data class PersonajeDatos(
    val id: Int,
    val nombre: String,
    val especie: String,
    val status: String,
    val imagen: String,
    val genero: String
)

fun PersonajeDatos.toEntity(): Character {
    return Character(
        id = id,
        nombre = nombre,
        especie = especie,
        status = status,
        imagen = imagen,
        genero = genero,
        favorito = false
    )
}
