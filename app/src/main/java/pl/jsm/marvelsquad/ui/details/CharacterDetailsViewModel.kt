package pl.jsm.marvelsquad.ui.details

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import pl.jsm.marvelsquad.data.Character
import pl.jsm.marvelsquad.data.CharactersComics
import pl.jsm.marvelsquad.data.Comic
import pl.jsm.marvelsquad.ui.base.BaseViewModel
import pl.jsm.marvelsquad.ui.details.CharacterDetailsNavigationAction.*
import pl.jsm.marvelsquad.usecase.FetchComicsForCharacterUseCase
import pl.jsm.marvelsquad.usecase.UpdateCharacterSquadStatusUseCase
import pl.jsm.marvelsquad.usecase.base.Status
import pl.jsm.marvelsquad.utils.asLiveDataStatus
import kotlin.math.max

class CharacterDetailsViewModel(
    private val character: Character,
    private val fetchComicsForCharacterUseCase: FetchComicsForCharacterUseCase,
    private val updateCharacterSquadStatusUseCase: UpdateCharacterSquadStatusUseCase
) : BaseViewModel<CharacterDetailsNavigationAction>() {
    val name = ObservableField<String>(character.name)
    val imageUrl = ObservableField<String>(character.thumbnailUrl)
    val hasDescription = ObservableBoolean(character.description.isNotBlank())
    val description = ObservableField<String>(character.description)
    val isHired = ObservableBoolean(character.isInSquad)

    val hasComics = ObservableBoolean(false)
    val newestComic = ObservableField<Comic?>()
    val secondNewestComic = ObservableField<Comic?>()
    val otherComicsCount = ObservableField(0)
    val hasOtherComics = ObservableBoolean(false)

    val shouldAnimateColorChange = ObservableBoolean(false)
    val isLoadingComics = ObservableBoolean(false)
    val showNoComicsDataMessage = ObservableBoolean(false)

    init {
        fetchComicsForCharacter()
    }

    fun hireFireButtonClicked() {
        shouldAnimateColorChange.set(true)
        val newValue = !isHired.get()
        val characterId = character.characterId

        updateCharacterSquadStatusUseCase::execute.asLiveDataStatus(characterId, newValue)
            .observeForever {
                when (it) {
                    is Status.Result -> handleHireStatusChanged()
                    is Status.Failure -> handleFireHireButtonError()
                }
            }
    }

    private fun handleHireStatusChanged() {
        isHired.set(!isHired.get())
        pushNavigationAction(NotifyCharacterChanged(character.copy(isInSquad = isHired.get())))
    }

    private fun fetchComicsForCharacter() {
        fetchComicsForCharacterUseCase::execute.asLiveDataStatus(character.characterId)
            .observeForever {
                isLoadingComics.set(it is Status.Loading)
                when (it) {
                    is Status.Result -> handleComicsForCharacters(it.data)
                    is Status.Failure -> handleFetchingComicsError()
                }
            }
    }

    private fun handleComicsForCharacters(charactersComics: CharactersComics) {
        val sortedComics = charactersComics.comics.sortedByDescending { it.onSaleDate }
        hasComics.set(sortedComics.isNotEmpty())
        newestComic.set(sortedComics.getOrNull(0))
        secondNewestComic.set(sortedComics.getOrNull(1))
        val otherComics = max(0, sortedComics.size - 2)
        otherComicsCount.set(otherComics)
        hasOtherComics.set(otherComics > 0)
        showNoComicsDataMessage.set(!hasComics.get())
    }

    private fun handleFetchingComicsError() {
        pushNavigationAction(ShowComicsFetchingError)
        showNoComicsDataMessage.set(!hasComics.get())
    }

    private fun handleFireHireButtonError() {
        pushNavigationAction(ShowButtonError)
    }
}
