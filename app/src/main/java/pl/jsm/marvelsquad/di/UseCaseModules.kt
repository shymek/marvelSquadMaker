package pl.jsm.marvelsquad.di

import org.koin.dsl.module
import pl.jsm.marvelsquad.usecase.FetchCharactersUseCase
import pl.jsm.marvelsquad.usecase.FetchComicsForCharacterUseCase
import pl.jsm.marvelsquad.usecase.HasMoreCharactersUseCase
import pl.jsm.marvelsquad.usecase.UpdateCharacterSquadStatusUseCase
import pl.jsm.marvelsquad.usecase.FetchSquadCharactersUseCase

val useCaseModule = module {
    factory { FetchCharactersUseCase(get(), get()) }

    factory { HasMoreCharactersUseCase(get()) }

    factory { FetchSquadCharactersUseCase(get()) }

    factory { FetchComicsForCharacterUseCase(get()) }

    factory { UpdateCharacterSquadStatusUseCase(get()) }
}