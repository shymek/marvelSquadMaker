package pl.jsm.marvelsquad.ui.host

import androidx.lifecycle.Observer
import com.appmattus.kotlinfixture.kotlinFixture
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test
import pl.jsm.marvelsquad.base.BaseTestCase
import pl.jsm.marvelsquad.data.Character
import pl.jsm.marvelsquad.ui.host.MainNavigationAction.UpdateCharacter

@ExperimentalCoroutinesApi
class MainViewModelTest : BaseTestCase() {

    private val fixture = kotlinFixture {
        factory<Character> {
            Character(fixture(), fixture(), fixture(), fixture(), fixture())
        }
    }

    private lateinit var viewModel: MainViewModel
    private val navigationActionObserver = mock<Observer<MainNavigationAction>>()

    @Before
    fun before() {
        viewModel = MainViewModel()
    }

    @Test
    fun `notifyCharacterChanged invoked the event`() = runBlockingTest {
        viewModel.navigationActions.observeForever(navigationActionObserver)
        val mockCharacter: Character = fixture()

        viewModel.notifyCharacterChanged(mockCharacter)

        verify(navigationActionObserver).onChanged(UpdateCharacter(mockCharacter))
        verifyNoMoreInteractions(navigationActionObserver)
    }
}