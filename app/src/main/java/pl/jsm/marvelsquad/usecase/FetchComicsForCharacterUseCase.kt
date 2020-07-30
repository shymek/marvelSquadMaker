package pl.jsm.marvelsquad.usecase

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import pl.jsm.marvelsquad.data.CharactersComics
import pl.jsm.marvelsquad.data.Comic
import pl.jsm.marvelsquad.data.repository.comics.ComicOrderBy.OnSaleDateAsc
import pl.jsm.marvelsquad.data.repository.comics.ComicsRepository
import pl.jsm.marvelsquad.data.toEntity

class FetchComicsForCharacterUseCase(
    private val comicsRepository: ComicsRepository
) {

    suspend fun execute(characterId: Long) =
        withContext(Dispatchers.IO) {
            val dbComics = comicsRepository.fetchFromDb(characterId)
            return@withContext if (dbComics.hasComics()) {
                dbComics
            } else {
                fetchAndSaveFromRemote(characterId)
            }
        }

    private suspend fun fetchAndSaveFromRemote(characterId: Long): CharactersComics {
        comicsRepository.storeComicsInDb(fetchComicsForCharacterFromApi(characterId), characterId)
        return comicsRepository.fetchFromDb(characterId)
    }

    private suspend fun fetchComicsForCharacterFromApi(characterId: Long): List<Comic> =
        comicsRepository.fetchComicsForCharacterFromApi(characterId, LIMIT, OnSaleDateAsc)
            .data.comics.map { it.toEntity() }

    private fun CharactersComics.hasComics(): Boolean =
        comics.isNotEmpty()

    companion object {
        private const val LIMIT = 100
    }
}