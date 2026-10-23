package com.example.movilab7.core.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import com.example.movilab7.core.room.Character

@Dao
interface CharacterDao {
    @Query("SELECT * FROM `Character` ORDER BY id ASC")
    fun getCharacters(): Flow<List<Character>>

    @Query("SELECT * FROM `Character` WHERE id = :id")
    fun getCharacterById(id: Int): Flow<Character?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCharacter(character: Character)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCharacters(characters: List<Character>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertInitialCharacters(characters: List<Character>)

    @Update
    suspend fun updateCharacter(character: Character)

    @Delete
    suspend fun deleteCharacter(character: Character)

    @Query("DELETE FROM `Character`")
    suspend fun deleteAllCharacters()

    @Query("UPDATE `Character` SET favorito = :favorito WHERE id = :id")
    suspend fun updateFavorito(id: Int, favorito: Boolean)
}