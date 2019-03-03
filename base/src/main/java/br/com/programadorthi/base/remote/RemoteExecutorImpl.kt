package br.com.programadorthi.base.remote

import br.com.programadorthi.base.exception.BaseException
import io.reactivex.Completable
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.functions.Consumer
import io.reactivex.functions.Function

class RemoteExecutorImpl(
    private val crashConsumer: Consumer<Throwable>,
    private val networkHandler: NetworkHandler,
    private val scheduler: Scheduler
) : RemoteExecutor {

    /**
     * Check for internet connection and execute the body function or emit an exception
     *
     * @param body Another function to execute when there is internet connection
     * @return An [Completable] with [BaseException.NoInternetConnectionException] when there is no internet
     * connection or with the body function result
     */
    override fun checkConnectionAndThenComplete(body: () -> Completable): Completable {
        return when (networkHandler.hasInternetConnection()) {
            false -> Completable.error(BaseException.NoInternetConnectionException())
            true -> body()
                .subscribeOn(scheduler)
                .doOnError(crashConsumer)
        }
    }

    /**
     * Check for internet connection and execute the body function or emit an exception
     *
     * @param body Another function to execute when there is internet connection
     * @return An [Completable] with [BaseException.NoInternetConnectionException] when there is no internet
     * connection or with the body function result
     */
    override fun <T> checkConnectionAndThenSingle(body: () -> Single<T>): Single<T> {
        return when (networkHandler.hasInternetConnection()) {
            false -> Single.error(BaseException.NoInternetConnectionException())
            true -> body()
                .subscribeOn(scheduler)
                .doOnError(crashConsumer)
        }
    }

    /**
     * Check for internet connection, execute the body function and map the result or emit an exception
     *
     * @param mapper A function to map values from server to feature models
     * @param body Another function to execute when there is internet connection
     * @return An [Completable] with [BaseException.NoInternetConnectionException] when there is no internet
     * connection or with the body function result
     */
    override fun <T, R> checkConnectionAndThenMapper(
        mapper: Function<T, R>,
        body: () -> Single<T>
    ): Single<R> {
        return when (networkHandler.hasInternetConnection()) {
            false -> Single.error(BaseException.NoInternetConnectionException())
            true -> body()
                .subscribeOn(scheduler)
                .map(mapper)
                .doOnError(crashConsumer)
        }
    }
}