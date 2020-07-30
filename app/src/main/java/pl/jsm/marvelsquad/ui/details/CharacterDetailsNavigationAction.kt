package pl.jsm.marvelsquad.ui.details

import pl.jsm.marvelsquad.data.Character

sealed class CharacterDetailsNavigationAction {
    object ShowComicsFetchingError : CharacterDetailsNavigationAction()
    object ShowButtonError : CharacterDetailsNavigationAction()
    data class NotifyCharacterChanged(val character: Character) : CharacterDetailsNavigationAction()
}