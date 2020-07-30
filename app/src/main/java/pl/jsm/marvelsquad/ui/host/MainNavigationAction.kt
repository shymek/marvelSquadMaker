package pl.jsm.marvelsquad.ui.host

import pl.jsm.marvelsquad.data.Character

sealed class MainNavigationAction {
    data class UpdateCharacter(val character: Character) : MainNavigationAction()
}