package pl.jsm.marvelsquad.usecase

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import pl.jsm.marvelsquad.data.repository.characters.CharactersRepositoryCache
import pl.jsm.marvelsquad.usecase.base.Status

class HasMoreCharactersUseCase(
    private val charactersRepositoryCache: CharactersRepositoryCache
) {

    fun hasMore(): LiveData<Status<Boolean>> = liveData {
        emit(Status.Loading)
        val lastTotal = charactersRepositoryCache.getLastTotal()
        val lastOffset = charactersRepositoryCache.getOffset()
        emit(Status.Result((lastTotal - lastOffset) > 0))
    }
}