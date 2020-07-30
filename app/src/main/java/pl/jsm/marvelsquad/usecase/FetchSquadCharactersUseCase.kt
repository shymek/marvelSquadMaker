package pl.jsm.marvelsquad.usecase

import pl.jsm.marvelsquad.data.Character
import pl.jsm.marvelsquad.data.repository.characters.CharactersRepository

class FetchSquadCharactersUseCase(
    private val charactersRepository: CharactersRepository
) {
    suspend fun getSquadCharacters(): List<Character> =
        charactersRepository.getCharactersFromSquad()
}