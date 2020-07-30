package pl.jsm.marvelsquad.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import pl.jsm.marvelsquad.BuildConfig
import pl.jsm.marvelsquad.net.AuthInterceptor
import pl.jsm.marvelsquad.net.CharacterDtoModel
import pl.jsm.marvelsquad.net.ComicDtoModel
import pl.jsm.marvelsquad.net.MarvelService
import pl.jsm.marvelsquad.net.gson.CharacterDeserializer
import pl.jsm.marvelsquad.net.gson.ComicDeserializer
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val networkModule = module {
    factory { AuthInterceptor() }
    factory { provideGson() }
    factory { provideLoggingInterceptor() }
    factory { provideOkHttpClient(get(), get()) }
    factory { provideMarvelApi(get()) }
    single { provideRetrofit(get(), get()) }
}

fun provideRetrofit(okHttpClient: OkHttpClient, gson: Gson): Retrofit =
    Retrofit.Builder()
        .baseUrl(BuildConfig.API_BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

fun provideOkHttpClient(
    authInterceptor: AuthInterceptor,
    loggingInterceptor: HttpLoggingInterceptor
): OkHttpClient =
    OkHttpClient.Builder()
        .addInterceptor(authInterceptor)
        .addInterceptor(loggingInterceptor)
        .build()

fun provideMarvelApi(retrofit: Retrofit): MarvelService = retrofit.create(MarvelService::class.java)

fun provideLoggingInterceptor(): HttpLoggingInterceptor {
    val interceptor = HttpLoggingInterceptor()
    interceptor.level = HttpLoggingInterceptor.Level.BODY

    return interceptor
}

fun provideGson(): Gson {
    val gsonBuilder = GsonBuilder()

    gsonBuilder.registerTypeAdapter(CharacterDtoModel::class.java, CharacterDeserializer())
    gsonBuilder.registerTypeAdapter(ComicDtoModel::class.java, ComicDeserializer())

    return gsonBuilder.create()
}