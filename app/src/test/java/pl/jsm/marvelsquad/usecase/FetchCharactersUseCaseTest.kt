package pl.jsm.marvelsquad.usecase

import com.appmattus.kotlinfixture.kotlinFixture
import com.nhaarman.mockitokotlin2.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import pl.jsm.marvelsquad.base.BaseTestCase
import pl.jsm.marvelsquad.data.Character
import pl.jsm.marvelsquad.data.repository.characters.CharactersRepository
import pl.jsm.marvelsquad.data.repository.characters.CharactersRepositoryCache
import pl.jsm.marvelsquad.data.toEntity
import pl.jsm.marvelsquad.net.CharactersResponseDataModel

@Suppress("BlockingMethodInNonBlockingContext")
@ExperimentalCoroutinesApi
class FetchCharactersUseCaseTest : BaseTestCase() {

    private val fixture = kotlinFixture {
        factory<Character> {
            Character(fixture(), fixture(), fixture(), fixture(), fixture())
        }
    }

    private lateinit var useCase: FetchCharactersUseCase

    private val mockCharactersRepository: CharactersRepository = mock()
    private val mockCharactersRepositoryCache: CharactersRepositoryCache = mock()

    @Before
    override fun setUp() {
        super.setUp()

        useCase = FetchCharactersUseCase(mockCharactersRepository, mockCharactersRepositoryCache)
    }

    @After
    override fun tearDown() {
        super.tearDown()

        verifyNoMoreInteractions(mockCharactersRepository)
        verifyNoMoreInteractions(mockCharactersRepositoryCache)
    }

    @Test
    fun `localFirst has data in db`() = runBlockingTest {
        val mockedCharacters: List<Character> = fixture()
        whenever(mockCharactersRepositoryCache.getOffset()).thenReturn(100)
        whenever(mockCharactersRepository.fetchFromDb()).thenReturn(mockedCharacters)

        val returnedCharacters = runBlocking { useCase.execute(true) }

        assertEquals(returnedCharacters, mockedCharacters)
        verify(mockCharactersRepository).fetchFromDb()
        verify(mockCharactersRepositoryCache).getOffset()
    }

    @Test
    fun `localFirst no offset storing result in db`() = runBlockingTest {
        val mockedApiResponse: CharactersResponseDataModel = mock()
        whenever(mockCharactersRepositoryCache.getOffset()).thenReturn(0)
        whenever(mockCharactersRepository.fetchFromRemote(any(), any())).thenReturn(
            mockedApiResponse
        )

        val returnedCharacters = runBlocking { useCase.execute(true) }

        assertEquals(returnedCharacters, mockedApiResponse.characters.map { it.toEntity() })
        verify(mockCharactersRepository).fetchFromRemote(0, 100)
        verify(mockCharactersRepositoryCache).getOffset()
        verify(mockCharactersRepositoryCache).storeOffset(mockedApiResponse.offset + 100)
        verify(mockCharactersRepositoryCache).storeLastTotal(mockedApiResponse.total)
        verify(mockCharactersRepository).storeInDb(mockedApiResponse.characters.map { it.toEntity() })
    }

}