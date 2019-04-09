package br.com.programadorthi.base.remote

import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.functions.Function

interface RemoteExecutor {

    fun checkConnectionAndThenComplete(action: Completable): Completable

    fun <T> checkConnectionAndThenSingle(action: Single<T>): Single<T>

    fun <T, R> checkConnectionAndThenMapper(mapper: Function<T, R>, action: Single<T>): Single<R>
}