package pl.jsm.marvelsquad.usecase.base

sealed class Status<out T> {
    data class Result<T>(val data: T) : Status<T>()
    object Loading : Status<Nothing>()
    data class Failure(val exception: Throwable) : Status<Nothing>()
}