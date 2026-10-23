package com.example.movilab7.core.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Character(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "nombre") val nombre: String?,
    @ColumnInfo(name = "especie") val especie: String?,
    @ColumnInfo(name = "status") val status: String?,
    @ColumnInfo(name = "imagen") val imagen: String?,
    @ColumnInfo(name = "genero") val genero: String?,
    @ColumnInfo(name = "favorito", defaultValue = "0") val favorito: Boolean = false
)