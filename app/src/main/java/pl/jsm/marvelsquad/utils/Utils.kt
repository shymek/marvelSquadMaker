package pl.jsm.marvelsquad.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import pl.jsm.marvelsquad.usecase.base.Status
import kotlin.reflect.KSuspendFunction0
import kotlin.reflect.KSuspendFunction1
import kotlin.reflect.KSuspendFunction2

fun <T> List<T>.getMissingElementsFrom(otherList: List<T>): List<T> {
    return otherList.filter { !contains(it) }
}

fun <T, R> KSuspendFunction1<T, R>.asLiveDataStatus(param: T): LiveData<Status<R>> =
    liveData {
        emit(Status.Loading)
        try {
            emit(Status.Result(this@asLiveDataStatus(param)))
        } catch (e: Throwable) {
            emit(Status.Failure(e))
        }
    }

fun <R> KSuspendFunction0<R>.asLiveDataStatus(): LiveData<Status<R>> =
    liveData {
        emit(Status.Loading)
        try {
            emit(Status.Result(this@asLiveDataStatus()))
        } catch (e: Throwable) {
            emit(Status.Failure(e))
        }
    }

fun <P1, P2, R> KSuspendFunction2<P1, P2, R>.asLiveDataStatus(
    firstParam: P1,
    secondParam: P2
): LiveData<Status<R>> =
    liveData {
        emit(Status.Loading)
        try {
            emit(Status.Result(this@asLiveDataStatus(firstParam, secondParam)))
        } catch (e: Throwable) {
            emit(Status.Failure(e))
        }
    }
