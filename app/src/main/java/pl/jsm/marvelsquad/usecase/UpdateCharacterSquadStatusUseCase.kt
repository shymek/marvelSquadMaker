package pl.jsm.marvelsquad.usecase

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import pl.jsm.marvelsquad.data.repository.comics.ComicsRepository

class UpdateCharacterSquadStatusUseCase(
    private val comicsRepository: ComicsRepository
) {
    suspend fun execute(characterId: Long, isInSquad: Boolean) =
        withContext(Dispatchers.IO) {
            comicsRepository.updateCharacterSquadStatus(
                characterId,
                isInSquad
            )
        }
}