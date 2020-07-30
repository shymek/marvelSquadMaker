package pl.jsm.marvelsquad.ui.details

import androidx.lifecycle.Observer
import com.appmattus.kotlinfixture.kotlinFixture
import com.nhaarman.mockitokotlin2.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Test
import pl.jsm.marvelsquad.base.BaseTestCase
import pl.jsm.marvelsquad.data.Character
import pl.jsm.marvelsquad.data.CharactersComics
import pl.jsm.marvelsquad.data.Comic
import pl.jsm.marvelsquad.ui.details.CharacterDetailsNavigationAction.*
import pl.jsm.marvelsquad.usecase.FetchComicsForCharacterUseCase
import pl.jsm.marvelsquad.usecase.UpdateCharacterSquadStatusUseCase
import java.lang.NullPointerException

@ExperimentalCoroutinesApi
class CharacterDetailsViewModelTest : BaseTestCase() {

    private val fixture = kotlinFixture {
        factory<Character> {
            Character(fixture(), fixture(), fixture(), fixture(), fixture())
        }
    }
    private lateinit var viewModel: CharacterDetailsViewModel
    private val navigationActionObserver = mock<Observer<CharacterDetailsNavigationAction>>()

    private var mockedCharacter: Character = fixture()
    private val mockFetchComicsForCharacterUseCase: FetchComicsForCharacterUseCase = mock()
    private val mockedUpdateCharacterSquadStatusUseCase: UpdateCharacterSquadStatusUseCase = mock()

    @Test
    fun `initializing viewModel with proper comics response`() = runBlockingTest {
        val charactersComics = CharactersComics(mockedCharacter, fixture())
        whenever(mockFetchComicsForCharacterUseCase.execute(any())).thenReturn(charactersComics)

        viewModel = CharacterDetailsViewModel(
            mockedCharacter,
            mockFetchComicsForCharacterUseCase,
            mockedUpdateCharacterSquadStatusUseCase
        )
        viewModel.navigationActions.observeForever(navigationActionObserver)

        assertViewModelCharacterSetProperly(mockedCharacter)
        assertEquals(viewModel.hasComics.get(), true)
        assertEquals(viewModel.newestComic.get(), getNewest(charactersComics.comics))
        assertEquals(viewModel.secondNewestComic.get(), getSecondNewest(charactersComics.comics))
        assertEquals(viewModel.otherComicsCount.get(), 3)
        assertEquals(viewModel.hasOtherComics.get(), true)
        assertEquals(viewModel.isLoadingComics.get(), false)
        verifyZeroInteractions(navigationActionObserver)
    }

    @Test
    fun `initializing viewModel with proper no comics response`() = runBlockingTest {
        val charactersComics = CharactersComics(mockedCharacter, emptyList())
        whenever(mockFetchComicsForCharacterUseCase.execute(any())).thenReturn(charactersComics)

        viewModel = CharacterDetailsViewModel(
            mockedCharacter,
            mockFetchComicsForCharacterUseCase,
            mockedUpdateCharacterSquadStatusUseCase
        )
        viewModel.navigationActions.observeForever(navigationActionObserver)

        assertViewModelCharacterSetProperly(mockedCharacter)
        assertEquals(viewModel.hasComics.get(), false)
        assertEquals(viewModel.newestComic.get(), null)
        assertEquals(viewModel.secondNewestComic.get(), null)
        assertEquals(viewModel.otherComicsCount.get(), 0)
        assertEquals(viewModel.hasOtherComics.get(), false)
        assertEquals(viewModel.isLoadingComics.get(), false)
        verifyZeroInteractions(navigationActionObserver)
    }

    @Test
    fun `initializing viewModel with one comic response`() = runBlockingTest {
        val charactersComics = CharactersComics(mockedCharacter, listOf(fixture()))
        whenever(mockFetchComicsForCharacterUseCase.execute(any())).thenReturn(charactersComics)

        viewModel = CharacterDetailsViewModel(
            mockedCharacter,
            mockFetchComicsForCharacterUseCase,
            mockedUpdateCharacterSquadStatusUseCase
        )
        viewModel.navigationActions.observeForever(navigationActionObserver)

        assertViewModelCharacterSetProperly(mockedCharacter)
        assertEquals(viewModel.hasComics.get(), true)
        assertEquals(viewModel.newestComic.get(), charactersComics.comics.first())
        assertEquals(viewModel.secondNewestComic.get(), null)
        assertEquals(viewModel.otherComicsCount.get(), 0)
        assertEquals(viewModel.hasOtherComics.get(), false)
        assertEquals(viewModel.isLoadingComics.get(), false)
        verifyZeroInteractions(navigationActionObserver)
    }

    @Test
    fun `initializing viewModel with exception when fetching comics`() = runBlockingTest {
        whenever(mockFetchComicsForCharacterUseCase.execute(any())).thenThrow(NullPointerException())

        viewModel = CharacterDetailsViewModel(
            mockedCharacter,
            mockFetchComicsForCharacterUseCase,
            mockedUpdateCharacterSquadStatusUseCase
        )
        viewModel.navigationActions.observeForever(navigationActionObserver)

        assertViewModelCharacterSetProperly(mockedCharacter)
        assertEquals(viewModel.hasComics.get(), false)
        assertEquals(viewModel.newestComic.get(), null)
        assertEquals(viewModel.secondNewestComic.get(), null)
        assertEquals(viewModel.otherComicsCount.get(), 0)
        assertEquals(viewModel.hasOtherComics.get(), false)
        assertEquals(viewModel.isLoadingComics.get(), false)
        assertEquals(viewModel.showNoComicsDataMessage.get(), true)

        verify(navigationActionObserver).onChanged(ShowComicsFetchingError)
        verifyNoMoreInteractions(navigationActionObserver)
    }

    @Test
    fun `tapping fireHire button changes the state and sends out event`() = runBlockingTest {
        val charactersComics = CharactersComics(mockedCharacter, fixture())
        whenever(mockFetchComicsForCharacterUseCase.execute(any())).thenReturn(charactersComics)

        viewModel = CharacterDetailsViewModel(
            mockedCharacter,
            mockFetchComicsForCharacterUseCase,
            mockedUpdateCharacterSquadStatusUseCase
        )
        viewModel.navigationActions.observeForever(navigationActionObserver)
        viewModel.hireFireButtonClicked()

        assertViewModelCharacterSetProperly(mockedCharacter, !mockedCharacter.isInSquad)

        verify(navigationActionObserver).onChanged(
            NotifyCharacterChanged(
                mockedCharacter.copy(
                    isInSquad = !mockedCharacter.isInSquad
                )
            )
        )
        verifyNoMoreInteractions(navigationActionObserver)
    }

    @Test
    fun `tapping fireHire button and there's an error when updating squad`() = runBlockingTest {
        val charactersComics = CharactersComics(mockedCharacter, fixture())
        whenever(mockFetchComicsForCharacterUseCase.execute(any())).thenReturn(charactersComics)
        whenever(mockedUpdateCharacterSquadStatusUseCase.execute(any(), any())).thenThrow(NullPointerException())

        viewModel = CharacterDetailsViewModel(
            mockedCharacter,
            mockFetchComicsForCharacterUseCase,
            mockedUpdateCharacterSquadStatusUseCase
        )
        viewModel.navigationActions.observeForever(navigationActionObserver)
        viewModel.hireFireButtonClicked()

        assertViewModelCharacterSetProperly(mockedCharacter)

        verify(navigationActionObserver).onChanged(ShowButtonError)
        verifyNoMoreInteractions(navigationActionObserver)
    }

    private fun assertViewModelCharacterSetProperly(
        character: Character,
        isInSquad: Boolean = character.isInSquad
    ) {
        assertEquals(viewModel.name.get(), character.name)
        assertEquals(viewModel.imageUrl.get(), character.thumbnailUrl)
        assertEquals(viewModel.hasDescription.get(), character.description.isNotBlank())
        assertEquals(viewModel.description.get(), character.description)
        assertEquals(viewModel.isHired.get(), isInSquad)
    }

    private fun getNewest(comics: List<Comic>): Comic {
        return comics.sortedByDescending { it.onSaleDate }[0]
    }

    private fun getSecondNewest(comics: List<Comic>): Comic {
        return comics.sortedByDescending { it.onSaleDate }[1]
    }
}