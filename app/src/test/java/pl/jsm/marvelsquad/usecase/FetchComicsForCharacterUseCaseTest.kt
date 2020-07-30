package pl.jsm.marvelsquad.usecase

import com.appmattus.kotlinfixture.kotlinFixture
import com.nhaarman.mockitokotlin2.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import pl.jsm.marvelsquad.base.BaseTestCase

import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import pl.jsm.marvelsquad.data.CharactersComics
import pl.jsm.marvelsquad.data.repository.comics.ComicOrderBy
import pl.jsm.marvelsquad.data.repository.comics.ComicsRepository
import pl.jsm.marvelsquad.data.toEntity
import pl.jsm.marvelsquad.net.ComicsResponseModel

@Suppress("BlockingMethodInNonBlockingContext")
@ExperimentalCoroutinesApi
class FetchComicsForCharacterUseCaseTest : BaseTestCase() {

    private val fixture = kotlinFixture()

    private lateinit var useCase: FetchComicsForCharacterUseCase

    private val mockComicsRepository: ComicsRepository = mock()

    @Before
    override fun setUp() {
        super.setUp()

        useCase = FetchComicsForCharacterUseCase(mockComicsRepository)
    }

    @After
    override fun tearDown() {
        super.tearDown()

        verifyNoMoreInteractions(mockComicsRepository)
    }

    @Test
    fun `execute with comics in db`() = runBlockingTest {
        val mockCharacterComics: CharactersComics = fixture()
        whenever(mockComicsRepository.fetchFromDb(any())).thenReturn(mockCharacterComics)

        val returnedComics = runBlocking { useCase.execute(1) }

        verify(mockComicsRepository).fetchFromDb(1)
        assertEquals(returnedComics, mockCharacterComics)
    }

    @Test
    fun `execute without comics in db`() = runBlockingTest {
        val mockDbResponse = CharactersComics(fixture(), emptyList())
        val mockApiResponse: ComicsResponseModel = fixture()
        val mappedApiComics =
            CharactersComics(fixture(), mockApiResponse.data.comics.map { it.toEntity() })
        whenever(mockComicsRepository.fetchFromDb(any())).thenReturn(mockDbResponse)
            .thenReturn(mappedApiComics)
        whenever(
            mockComicsRepository.fetchComicsForCharacterFromApi(
                any(),
                any(),
                any()
            )
        ).thenReturn(mockApiResponse)

        val returnedComics = runBlocking { useCase.execute(1) }

        assertEquals(mappedApiComics, returnedComics)
        verify(mockComicsRepository, times(2)).fetchFromDb(any())
        verify(mockComicsRepository).storeComicsInDb(mappedApiComics.comics, 1)
        verify(mockComicsRepository).fetchComicsForCharacterFromApi(
            1,
            100,
            ComicOrderBy.OnSaleDateAsc
        )
    }
}