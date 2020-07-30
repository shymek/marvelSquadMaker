package pl.jsm.marvelsquad.ui.characterslist

import pl.jsm.marvelsquad.data.Character

sealed class CharactersListNavigationAction {
    data class NavigateToCharacterDetails(val character: Character) : CharactersListNavigationAction()
    object ShowCharactersLoadingError : CharactersListNavigationAction()
    object ShowSquadLoadingError : CharactersListNavigationAction()
}
