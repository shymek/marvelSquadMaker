package pl.jsm.marvelsquad.data.db.dao

import androidx.room.*
import pl.jsm.marvelsquad.data.Character
import pl.jsm.marvelsquad.data.CharactersComics
import pl.jsm.marvelsquad.data.ComicsCharacterCrossRef

@Dao
interface CharacterDao {

    @Query("SELECT * FROM character")
    suspend fun getAll(): List<Character>

    @Query("SELECT * FROM character WHERE isInSquad = 1")
    suspend fun getSquadCharacters(): List<Character>

    @Query("UPDATE character SET isInSquad = :isInSquad WHERE characterId = :characterId")
    suspend fun changeCharacterSquadStatus(characterId: Long, isInSquad: Boolean)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(characters: List<Character>)

    @Transaction
    @Query("SELECT * FROM character WHERE characterId = :characterId")
    suspend fun getComicsForCharacter(characterId: Long): List<CharactersComics>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllComicsForCharacter(charactersComics: List<ComicsCharacterCrossRef>)

}