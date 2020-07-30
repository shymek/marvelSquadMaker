package pl.jsm.marvelsquad.di

import org.koin.dsl.module
import pl.jsm.marvelsquad.data.repository.characters.CharactersRepository
import pl.jsm.marvelsquad.data.repository.comics.ComicsRepository

val storageModule = module {
    factory {
        CharactersRepository(
            get(),
            get()
        )
    }
    factory {
        ComicsRepository(
            get(),
            get(),
            get()
        )
    }
}