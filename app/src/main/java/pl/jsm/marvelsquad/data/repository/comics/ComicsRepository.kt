package pl.jsm.marvelsquad.data.repository.comics

import pl.jsm.marvelsquad.data.CharactersComics
import pl.jsm.marvelsquad.data.Comic
import pl.jsm.marvelsquad.data.ComicsCharacterCrossRef
import pl.jsm.marvelsquad.data.db.dao.CharacterDao
import pl.jsm.marvelsquad.data.db.dao.ComicDao
import pl.jsm.marvelsquad.net.ComicsResponseModel
import pl.jsm.marvelsquad.net.MarvelService

class ComicsRepository(
    private val marvelService: MarvelService,
    private val characterDao: CharacterDao,
    private val comicDao: ComicDao
) {

    suspend fun updateCharacterSquadStatus(characterId: Long, isInSquad: Boolean) =
        characterDao.changeCharacterSquadStatus(characterId, isInSquad)

    suspend fun fetchFromDb(characterId: Long): CharactersComics =
        characterDao.getComicsForCharacter(characterId).first()

    suspend fun fetchComicsForCharacterFromApi(
        characterId: Long,
        limit: Int,
        orderBy: ComicOrderBy
    ): ComicsResponseModel =
        marvelService.getComicsForCharacter(
            characterId,
            limit,
            orderBy.apiParameter
        )

    suspend fun storeComicsInDb(comics: List<Comic>, characterId: Long) {
        comicDao.insertAll(comics)
        val comicsForCharacters = comics.map { ComicsCharacterCrossRef(it.comicId, characterId) }
        characterDao.insertAllComicsForCharacter(comicsForCharacters)
    }

    private fun CharactersComics.hasComics(): Boolean =
        comics.isNotEmpty()
}
