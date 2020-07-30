package pl.jsm.marvelsquad.ui.base

import androidx.lifecycle.ViewModel

abstract class BaseViewModel<ACTION : Any> : ViewModel() {
    val navigationActions = SingleLiveEvent<ACTION>()

    protected fun pushNavigationAction(action: ACTION) {
        navigationActions.value = action
    }
}