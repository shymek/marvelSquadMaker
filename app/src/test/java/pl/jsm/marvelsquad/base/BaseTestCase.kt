package pl.jsm.marvelsquad.base

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule

@ExperimentalCoroutinesApi
abstract class BaseTestCase {

    @get:Rule
    val rule = InstantTaskExecutorRule()


    @Before
    open fun setUp() {
        Dispatchers.setMain(Dispatchers.Unconfined)
    }

    @After
    open fun tearDown() {
        Dispatchers.resetMain() // reset main dispatcher to the original Main dispatcher
    }
}