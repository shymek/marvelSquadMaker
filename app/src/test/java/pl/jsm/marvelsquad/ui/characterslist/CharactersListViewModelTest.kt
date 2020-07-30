package pl.jsm.marvelsquad.ui.characterslist

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.appmattus.kotlinfixture.kotlinFixture
import com.nhaarman.mockitokotlin2.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Test
import pl.jsm.marvelsquad.base.BaseTestCase
import pl.jsm.marvelsquad.data.Character
import pl.jsm.marvelsquad.ui.characterslist.CharactersListNavigationAction.*
import pl.jsm.marvelsquad.usecase.FetchCharactersUseCase
import pl.jsm.marvelsquad.usecase.HasMoreCharactersUseCase
import pl.jsm.marvelsquad.usecase.FetchSquadCharactersUseCase
import pl.jsm.marvelsquad.usecase.base.Status

@ExperimentalCoroutinesApi
class CharactersListViewModelTest : BaseTestCase() {

    private val fixture = kotlinFixture {
        factory<Character> {
            Character(fixture(), fixture(), fixture(), fixture(), fixture())
        }
    }
    private val squadFixture = kotlinFixture {
        factory<Character> {
            Character(fixture(), fixture(), fixture(), fixture(), true)
        }
    }

    private lateinit var viewModel: CharactersListViewModel
    private val navigationActionObserver = mock<Observer<CharactersListNavigationAction>>()

    private val mockFetchCharactersUseCase: FetchCharactersUseCase = mock()
    private val mockHasMoreCharactersUseCase: HasMoreCharactersUseCase = mock()
    private val mockFetchSquadCharactersUseCase: FetchSquadCharactersUseCase = mock()

    @Test
    fun `initializing viewModel with proper characters response and no squad characters`() =
        runBlockingTest {
            val mockedCharacters: List<Character> = fixture()
            whenever(mockFetchCharactersUseCase.execute(any())).thenReturn(mockedCharacters)
            whenever(mockFetchSquadCharactersUseCase.getSquadCharacters()).thenReturn(emptyList())
            whenever(mockHasMoreCharactersUseCase.hasMore()).thenReturn(
                MutableLiveData(Status.Result(true))
            )

            viewModel = CharactersListViewModel(
                mockFetchCharactersUseCase,
                mockHasMoreCharactersUseCase,
                mockFetchSquadCharactersUseCase
            )
            viewModel.navigationActions.observeForever(navigationActionObserver)

            assertEquals(viewModel.characters, mockedCharacters.sortedBy { it.name })
            assertEquals(viewModel.squadCharacters.isEmpty(), true)
            assertEquals(viewModel.hasSquadMembers.get(), false)
            assertEquals(viewModel.showNoCharactersMessage.get(), false)
            assertNotLoading()

            verifyZeroInteractions(navigationActionObserver)
        }

    @Test
    fun `initializing viewModel with proper characters response and some squad characters`() =
        runBlockingTest {
            val mockedCharacters: List<Character> = fixture()
            val squadCharacters: List<Character> = squadFixture()
            whenever(mockFetchCharactersUseCase.execute(any())).thenReturn(mockedCharacters)
            whenever(mockFetchSquadCharactersUseCase.getSquadCharacters()).thenReturn(
                squadCharacters
            )
            whenever(mockHasMoreCharactersUseCase.hasMore()).thenReturn(
                MutableLiveData(Status.Result(true))
            )

            viewModel = CharactersListViewModel(
                mockFetchCharactersUseCase,
                mockHasMoreCharactersUseCase,
                mockFetchSquadCharactersUseCase
            )
            viewModel.navigationActions.observeForever(navigationActionObserver)

            assertEquals(viewModel.characters, mockedCharacters.sortedBy { it.name })
            assertEquals(viewModel.squadCharacters, squadCharacters.sortedBy { it.name })
            assertEquals(viewModel.hasSquadMembers.get(), true)
            assertEquals(viewModel.showNoCharactersMessage.get(), false)
            assertNotLoading()

            verifyZeroInteractions(navigationActionObserver)
        }

    @Test
    fun `initializing viewModel with no characters`() =
        runBlockingTest {
            val squadCharacters: List<Character> = squadFixture()

            whenever(mockFetchCharactersUseCase.execute(any())).thenReturn(emptyList())
            whenever(mockFetchSquadCharactersUseCase.getSquadCharacters()).thenReturn(
                squadCharacters
            )
            whenever(mockHasMoreCharactersUseCase.hasMore()).thenReturn(
                MutableLiveData(Status.Result(true))
            )

            viewModel = CharactersListViewModel(
                mockFetchCharactersUseCase,
                mockHasMoreCharactersUseCase,
                mockFetchSquadCharactersUseCase
            )
            viewModel.navigationActions.observeForever(navigationActionObserver)

            assertEquals(viewModel.characters, emptyList<Character>())
            assertEquals(viewModel.squadCharacters, squadCharacters.sortedBy { it.name })
            assertEquals(viewModel.hasSquadMembers.get(), true)
            assertEquals(viewModel.showNoCharactersMessage.get(), true)
            assertNotLoading()

            verifyZeroInteractions(navigationActionObserver)
        }

    @Test
    fun `initializing viewModel with error on fetching characters`() =
        runBlockingTest {
            val squadCharacters: List<Character> = fixture()

            whenever(mockFetchCharactersUseCase.execute(any())).thenThrow(NullPointerException())
            whenever(mockFetchSquadCharactersUseCase.getSquadCharacters()).thenReturn(
                squadCharacters
            )
            whenever(mockHasMoreCharactersUseCase.hasMore()).thenReturn(
                MutableLiveData(Status.Result(true))
            )

            viewModel = CharactersListViewModel(
                mockFetchCharactersUseCase,
                mockHasMoreCharactersUseCase,
                mockFetchSquadCharactersUseCase
            )
            viewModel.navigationActions.observeForever(navigationActionObserver)

            assertEquals(viewModel.characters, emptyList<Character>())
            assertEquals(viewModel.squadCharacters, squadCharacters.sortedBy { it.name })
            assertEquals(viewModel.hasSquadMembers.get(), true)
            assertEquals(viewModel.showNoCharactersMessage.get(), true)
            assertNotLoading()

            verify(navigationActionObserver).onChanged(ShowCharactersLoadingError)
            verifyNoMoreInteractions(navigationActionObserver)
        }

    @Test
    fun `initializing viewModel with error on fetching squad`() =
        runBlockingTest {
            val characters: List<Character> = fixture()

            whenever(mockFetchCharactersUseCase.execute(any())).thenReturn(characters)
            whenever(mockFetchSquadCharactersUseCase.getSquadCharacters()).thenThrow(
                NullPointerException()
            )
            whenever(mockHasMoreCharactersUseCase.hasMore()).thenReturn(
                MutableLiveData(Status.Result(true))
            )

            viewModel = CharactersListViewModel(
                mockFetchCharactersUseCase,
                mockHasMoreCharactersUseCase,
                mockFetchSquadCharactersUseCase
            )
            viewModel.navigationActions.observeForever(navigationActionObserver)

            assertEquals(viewModel.characters, characters.sortedBy { it.name })
            assertEquals(viewModel.squadCharacters, emptyList<Character>())
            assertEquals(viewModel.hasSquadMembers.get(), false)
            assertEquals(viewModel.showNoCharactersMessage.get(), false)
            assertNotLoading()

            verify(navigationActionObserver).onChanged(ShowSquadLoadingError)
            verifyNoMoreInteractions(navigationActionObserver)
        }

    @Test
    fun `characterClicked invoking navigate event`() =
        runBlockingTest {
            val mockedCharacters: List<Character> = fixture()
            val squadCharacters: List<Character> = squadFixture()

            whenever(mockFetchCharactersUseCase.execute(any())).thenReturn(mockedCharacters)
            whenever(mockFetchSquadCharactersUseCase.getSquadCharacters()).thenReturn(squadCharacters)
            whenever(mockHasMoreCharactersUseCase.hasMore()).thenReturn(
                MutableLiveData(Status.Result(true))
            )

            viewModel = CharactersListViewModel(
                mockFetchCharactersUseCase,
                mockHasMoreCharactersUseCase,
                mockFetchSquadCharactersUseCase
            )
            viewModel.navigationActions.observeForever(navigationActionObserver)

            viewModel.characterClicked(mockedCharacters.first())
            assertNotLoading()

            verify(navigationActionObserver).onChanged(NavigateToCharacterDetails(mockedCharacters.first()))
            verifyNoMoreInteractions(navigationActionObserver)
        }

    @Test
    fun `updateCharacter removing character from squad`() =
        runBlockingTest {
            val mockedCharacters: ArrayList<Character> = squadFixture()
            val squadCharacters: ArrayList<Character> = mockedCharacters
            val characterToRemove = mockedCharacters.first()
            val updatedList = ArrayList<Character>()
            updatedList.addAll(mockedCharacters)
            updatedList[0] = characterToRemove.copy(isInSquad = false)
            val updatedSquadList =
                squadCharacters.filter { it.characterId != characterToRemove.characterId }

            whenever(mockFetchCharactersUseCase.execute(any())).thenReturn(mockedCharacters)
            whenever(mockFetchSquadCharactersUseCase.getSquadCharacters()).thenReturn(squadCharacters)
            whenever(mockHasMoreCharactersUseCase.hasMore()).thenReturn(
                MutableLiveData(Status.Result(true))
            )

            viewModel = CharactersListViewModel(
                mockFetchCharactersUseCase,
                mockHasMoreCharactersUseCase,
                mockFetchSquadCharactersUseCase
            )
            viewModel.navigationActions.observeForever(navigationActionObserver)

            viewModel.updateCharacter(characterToRemove.copy(isInSquad = false))

            assertEquals(viewModel.characters, updatedList.sortedBy { it.name })
            assertEquals(viewModel.squadCharacters, updatedSquadList.sortedBy { it.name })
            verifyZeroInteractions(navigationActionObserver)
        }

    @Test
    fun `fetchMoreCharacters appendsNewCharacters`() =
        runBlockingTest {
            val mockedCharacters: List<Character> = fixture()
            val squadCharacters: List<Character> = squadFixture()
            val additionalCharacters: List<Character> = fixture()
            val combinedCharacters =
                mockedCharacters.sortedBy { it.name } + additionalCharacters.sortedBy { it.name }

            whenever(mockFetchCharactersUseCase.execute(any())).thenReturn(mockedCharacters)
            whenever(mockFetchSquadCharactersUseCase.getSquadCharacters()).thenReturn(squadCharacters)
            whenever(mockHasMoreCharactersUseCase.hasMore()).thenReturn(
                MutableLiveData(Status.Result(true))
            )

            viewModel = CharactersListViewModel(
                mockFetchCharactersUseCase,
                mockHasMoreCharactersUseCase,
                mockFetchSquadCharactersUseCase
            )
            viewModel.navigationActions.observeForever(navigationActionObserver)
            whenever(mockFetchCharactersUseCase.execute(any())).thenReturn(additionalCharacters)

            viewModel.fetchMoreCharacters()

            assertEquals(viewModel.characters, combinedCharacters)
            assertEquals(viewModel.squadCharacters, squadCharacters.sortedBy { it.name })
            assertEquals(viewModel.hasSquadMembers.get(), true)
            assertEquals(viewModel.showNoCharactersMessage.get(), false)
            assertNotLoading()

            verifyZeroInteractions(navigationActionObserver)
        }

    private fun assertNotLoading() {
        assertEquals(viewModel.isLoadingMore.get(), false)
        assertEquals(viewModel.isLoadingData.get(), false)
    }
}