package pl.jsm.marvelsquad.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import pl.jsm.marvelsquad.data.db.AppDatabase
import pl.jsm.marvelsquad.data.db.dao.CharacterDao
import pl.jsm.marvelsquad.data.db.dao.ComicDao
import pl.jsm.marvelsquad.data.repository.characters.CharactersRepositoryCache

val dataModule = module {
    single { provideDatabase(androidContext()) }
    single { provideCharacterDao(get()) }
    single { provideComicDao(get()) }
}

private fun provideDatabase(applicationContext: Context): AppDatabase {
    return Room.databaseBuilder(
        applicationContext,
        AppDatabase::class.java,
        "dbName"
    ).build()
}

private fun provideCharacterDao(appDatabase: AppDatabase): CharacterDao =
    appDatabase.characterDao()

private fun provideComicDao(appDatabase: AppDatabase): ComicDao =
    appDatabase.comicDao()


val preferencesModule = module {
    single { provideSharedPreferences(androidApplication()) }
    single { provideCharacterRepositoryCache(get()) }
}

private fun provideSharedPreferences(app: Application): SharedPreferences =
    app.getSharedPreferences(PREFERENCES_KEY, Context.MODE_PRIVATE)

private fun provideCharacterRepositoryCache(sharedPreferences: SharedPreferences) =
    CharactersRepositoryCache(
        sharedPreferences
    )

private const val PREFERENCES_KEY = "pl.jsm.marvelsquad.preferences"
