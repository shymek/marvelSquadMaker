package pl.jsm.marvelsquad.data.repository.characters

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import pl.jsm.marvelsquad.data.Character
import pl.jsm.marvelsquad.data.db.dao.CharacterDao
import pl.jsm.marvelsquad.net.CharactersResponseDataModel
import pl.jsm.marvelsquad.net.MarvelService

class CharactersRepository(
    private val marvelService: MarvelService,
    private val characterDao: CharacterDao
) {

    suspend fun getCharactersFromSquad(): List<Character> =
        withContext(Dispatchers.IO) { characterDao.getSquadCharacters() }

    suspend fun fetchFromDb() =
        characterDao.getAll()

    suspend fun fetchFromRemote(offset: Int, limit: Int): CharactersResponseDataModel =
        withContext(Dispatchers.IO) { marvelService.getHeroes(limit, offset).data }

    suspend fun storeInDb(characters: List<Character>) =
        characterDao.insertAll(characters)
}
