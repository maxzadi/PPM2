package com.example.movilab7.core.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface LocationDao {
    @Query("SELECT * FROM `LocationEntity` ORDER BY id ASC")
    fun getLocationEntitys(): Flow<List<LocationEntity>>

    @Query("SELECT * FROM `LocationEntity` WHERE id = :id")
    fun getLocationEntityById(id: Int): Flow<LocationEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLocationEntity(locationEntity: LocationEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLocationEntitys(locationEntitys: List<LocationEntity>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertInitialLocationEntitys(locationEntitys: List<LocationEntity>)

    @Update
    suspend fun updateLocationEntity(locationEntity: LocationEntity)

    @Delete
    suspend fun deleteLocationEntity(locationEntity: LocationEntity)

    @Query("DELETE FROM `LocationEntity`")
    suspend fun deleteAllLocationEntitys()
}