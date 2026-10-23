package com.example.movilab7.locations.data

import com.example.movilab7.characters.data.PersonajeDatos
import com.example.movilab7.core.room.LocationEntity

data class Location(
    val id: Int,
    val name: String,
    val type: String,
    val dimension: String
)

fun Location.toEntity(): com.example.movilab7.core.room.LocationEntity {
    return LocationEntity(
        id = id,
        name = name,
        type = type,
        dimension = dimension
    )
}