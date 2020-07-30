package pl.jsm.marvelsquad

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import pl.jsm.marvelsquad.di.*

class MarvelSquadApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@MarvelSquadApplication)
            modules(
                listOf(
                    storageModule, useCaseModule, networkModule, viewModelModule,
                    dataModule, preferencesModule
                )
            )
        }
    }
}
