package pl.jsm.marvelsquad.usecase

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import pl.jsm.marvelsquad.data.Character
import pl.jsm.marvelsquad.data.repository.characters.CharactersRepository
import pl.jsm.marvelsquad.data.repository.characters.CharactersRepositoryCache
import pl.jsm.marvelsquad.data.toEntity

class FetchCharactersUseCase(
    private val charactersRepository: CharactersRepository,
    private val charactersRepositoryCache: CharactersRepositoryCache
) {

    private val limit: Int = 100

    suspend fun execute(localFirst: Boolean) =
        withContext(Dispatchers.IO) {
            val offset = charactersRepositoryCache.getOffset()
            return@withContext if (localFirst && offset > 0) {
                charactersRepository.fetchFromDb()
            } else {
                fetchFromRemoteAndStore(offset, limit)
            }
        }

    private suspend fun fetchFromRemoteAndStore(offset: Int, limit: Int): List<Character> {
        val charactersResponseDataModel = charactersRepository.fetchFromRemote(offset, limit)
        val newOffset = charactersResponseDataModel.offset + limit
        charactersRepositoryCache.storeOffset(newOffset)
        charactersRepositoryCache.storeLastTotal(charactersResponseDataModel.total)
        val charactersList = charactersResponseDataModel.characters.map { it.toEntity() }
        charactersRepository.storeInDb(charactersList)

        return charactersList
    }
}
