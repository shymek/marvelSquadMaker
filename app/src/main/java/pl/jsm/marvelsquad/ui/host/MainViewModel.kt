package pl.jsm.marvelsquad.ui.host

import pl.jsm.marvelsquad.data.Character
import pl.jsm.marvelsquad.ui.base.BaseViewModel
import pl.jsm.marvelsquad.ui.host.MainNavigationAction.UpdateCharacter

class MainViewModel : BaseViewModel<MainNavigationAction>() {

    fun notifyCharacterChanged(character: Character) {
        pushNavigationAction(UpdateCharacter(character))
    }
}
