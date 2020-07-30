package pl.jsm.marvelsquad.ui.characterslist

import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import pl.jsm.marvelsquad.R
import pl.jsm.marvelsquad.data.Character
import pl.jsm.marvelsquad.ui.base.BaseViewModel
import pl.jsm.marvelsquad.ui.characterslist.CharactersListNavigationAction.*
import pl.jsm.marvelsquad.usecase.FetchCharactersUseCase
import pl.jsm.marvelsquad.usecase.HasMoreCharactersUseCase
import pl.jsm.marvelsquad.usecase.FetchSquadCharactersUseCase
import pl.jsm.marvelsquad.usecase.base.Status
import pl.jsm.marvelsquad.utils.asLiveDataStatus
import pl.jsm.marvelsquad.utils.getMissingElementsFrom

class CharactersListViewModel(
    private val fetchCharactersUseCase: FetchCharactersUseCase,
    private val hasMoreCharactersUseCase: HasMoreCharactersUseCase,
    private val fetchSquadCharactersUseCase: FetchSquadCharactersUseCase
) : BaseViewModel<CharactersListNavigationAction>() {
    val characters = ObservableArrayList<Character>()
    val squadCharacters = ObservableArrayList<Character>()
    val titleId = ObservableField<Int>(R.string.characters_title)
    val hasSquadMembers = ObservableBoolean()
    val showNoCharactersMessage = ObservableBoolean(false)
    val isLoadingData = ObservableBoolean()
    val isLoadingMore = ObservableBoolean()

    private var canLoadMore = true

    init {
        fetchCharacters()
        fetchSquadCharacters()
    }

    fun fetchCharacters(forceRefresh: Boolean = false) {
        if (characters.isNotEmpty() && !forceRefresh) return

        fetchCharactersFromRepository(true, forceRefresh)
    }

    fun fetchMoreCharacters() {
        if (canLoadMore && !isLoadingData.get()) {
            isLoadingMore.set(true)
            fetchCharactersFromRepository(false)
        }
    }

    fun characterClicked(character: Character) {
        pushNavigationAction(NavigateToCharacterDetails(character))
    }

    fun updateCharacter(character: Character) {
        characters.updateCharacter(character)
        if (character.isInSquad && !squadCharacters.contains(character)) {
            squadCharacters.add(character)
            squadCharacters.sortBy { it.name }
        } else if (!character.isInSquad) {
            squadCharacters.removeCharacter(character)
        }
        hasSquadMembers.set(squadCharacters.isNotEmpty())
    }

    private fun fetchSquadCharacters() {
        fetchSquadCharactersUseCase::getSquadCharacters.asLiveDataStatus()
            .observeForever {
                when (it) {
                    is Status.Result -> handleSquadCharacters(it.data)
                    is Status.Failure -> handleSquadError()
                }
            }
    }

    private fun fetchCharactersFromRepository(localFirst: Boolean, forceRefresh: Boolean = false) {
        fetchCharactersUseCase::execute.asLiveDataStatus(localFirst)
            .observeForever {
                isLoadingData.set(it is Status.Loading)
                when (it) {
                    is Status.Result -> handleCharacters(it.data, forceRefresh)
                    is Status.Failure -> handleError()
                }
            }
    }

    private fun handleCharacters(newCharacters: List<Character>, forceRefresh: Boolean) {
        if (forceRefresh) characters.clear()
        characters.addAll(characters.getMissingElementsFrom(newCharacters).sortedBy { it.name })
        checkIfCanLoadMore()
        setNotLoading()
    }

    private fun checkIfCanLoadMore() {
        hasMoreCharactersUseCase.hasMore()
            .observeForever {
                when (it) {
                    is Status.Result -> canLoadMore = it.data
                }
            }
    }

    private fun handleSquadCharacters(newSquadCharacters: List<Character>) {
        squadCharacters.addAll(newSquadCharacters.sortedBy { it.name })
        hasSquadMembers.set(squadCharacters.isNotEmpty())
    }

    private fun handleError() {
        setNotLoading()
        pushNavigationAction(ShowCharactersLoadingError)
    }

    private fun evaluateShowNoCharacters() {
        showNoCharactersMessage.set(characters.isEmpty() && !isLoadingData.get() && !isLoadingMore.get())
    }

    private fun handleSquadError() {
        pushNavigationAction(ShowSquadLoadingError)
    }

    private fun setNotLoading() {
        isLoadingData.set(false)
        isLoadingMore.set(false)
        evaluateShowNoCharacters()
    }

    private fun ObservableArrayList<Character>.removeCharacter(character: Character) {
        val position = positionOfCharacter(character)
        if (position >= 0) removeAt(positionOfCharacter(character))
    }

    private fun ObservableArrayList<Character>.updateCharacter(character: Character) {
        this[positionOfCharacter(character)] = character
    }

    private fun ObservableArrayList<Character>.positionOfCharacter(character: Character) =
        indexOf(find { it.characterId == character.characterId })

}