package br.com.programadorthi.base.remote

import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.functions.Function

interface RemoteExecutor {

    fun checkConnectionAndThenComplete(body: () -> Completable): Completable

    fun <T> checkConnectionAndThenSingle(body: () -> Single<T>): Single<T>

    fun <T, R> checkConnectionAndThenMapper(
        mapper: Function<T, R>,
        body: () -> Single<T>
    ): Single<R>
}