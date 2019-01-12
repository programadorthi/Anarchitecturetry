package br.com.programadorthi.base.network

import br.com.programadorthi.base.exception.BaseException
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.functions.Consumer
import io.reactivex.functions.Function
import io.reactivex.schedulers.Schedulers

class RemoteExecutorImpl(
    private val crashlyticsConsumer: Consumer<Throwable>,
    private val networkHandler: NetworkHandler
) : RemoteExecutor {

    /**
     * Check for internet connection and execute the body function or emit an exception
     *
     * @param body Another function to execute when there is internet connection
     * @return An [Completable] with [BaseException.NetworkException] when there is no internet
     * connection or with the body function result
     */
    override fun checkConnectionAndThenComplete(body: () -> Completable): Completable {
        return when (networkHandler.hasInternetConnection()) {
            false -> Completable.error(BaseException.NetworkException)
            true -> body()
                .subscribeOn(Schedulers.io())
                .doOnError(crashlyticsConsumer)
        }
    }

    /**
     * Check for internet connection and execute the body function or emit an exception
     *
     * @param body Another function to execute when there is internet connection
     * @return An [Completable] with [BaseException.NetworkException] when there is no internet
     * connection or with the body function result
     */
    override fun <T> checkConnectionAndThenSingle(body: () -> Single<T>): Single<T> {
        return when (networkHandler.hasInternetConnection()) {
            false -> Single.error(BaseException.NetworkException)
            true -> body()
                .subscribeOn(Schedulers.io())
                .doOnError(crashlyticsConsumer)
        }
    }

    /**
     * Check for internet connection, execute the body function and map the result or emit an exception
     *
     * @param mapper A function to map values from server to feature models
     * @param body Another function to execute when there is internet connection
     * @return An [Completable] with [BaseException.NetworkException] when there is no internet
     * connection or with the body function result
     */
    override fun <T, R> checkConnectionAndThenMapper(
        mapper: Function<T, R>,
        body: () -> Single<T>
    ): Single<R> {
        return when (networkHandler.hasInternetConnection()) {
            false -> Single.error(BaseException.NetworkException)
            true -> body()
                .subscribeOn(Schedulers.io())
                .map(mapper)
                .doOnError(crashlyticsConsumer)
        }
    }
}