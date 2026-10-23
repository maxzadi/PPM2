package com.example.movilab7.core

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.movilab7.core.room.Character
import com.example.movilab7.core.room.CharacterDao
import com.example.movilab7.core.room.LocationDao
import com.example.movilab7.core.room.LocationEntity


@Database(entities = [Character::class, LocationEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun characterDao(): CharacterDao
    abstract fun locationDao(): LocationDao
}
