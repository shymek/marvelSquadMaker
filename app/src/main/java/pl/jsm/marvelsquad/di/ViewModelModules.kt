package pl.jsm.marvelsquad.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import pl.jsm.marvelsquad.data.Character
import pl.jsm.marvelsquad.ui.characterslist.CharactersListViewModel
import pl.jsm.marvelsquad.ui.details.CharacterDetailsViewModel
import pl.jsm.marvelsquad.ui.host.MainViewModel

val viewModelModule = module {
    viewModel { MainViewModel() }
    viewModel { CharactersListViewModel(get(), get(), get()) }
    viewModel { (character: Character) -> CharacterDetailsViewModel(character, get(), get()) }
}
