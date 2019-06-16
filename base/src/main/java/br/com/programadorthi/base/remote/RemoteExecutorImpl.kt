package br.com.programadorthi.base.remote

import br.com.programadorthi.base.exception.BaseException
import br.com.programadorthi.base.exception.CrashConsumer
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class RemoteExecutorImpl(
    private val crashConsumer: CrashConsumer,
    private val networkHandler: NetworkHandler,
    private val dispatcher: CoroutineDispatcher
) : RemoteExecutor {

    override suspend fun checkConnectionAndThenDone(action: suspend () -> Unit) {
        validateInternetConnection()

        withContext(dispatcher) {
            try {
                action()
            } catch (ex: Throwable) {
                crashConsumer.report(ex)
                throw ex
            }
        }
    }

    override suspend fun <T> checkConnectionAndThenReturn(action: suspend () -> T): T {
        validateInternetConnection()

        return withContext(dispatcher) {
            try {
                action()
            } catch (ex: Throwable) {
                crashConsumer.report(ex)
                throw ex
            }
        }
    }

    override suspend fun <T, R> checkConnectionMapperAndThenReturn(
        mapper: BaseRemoteMapper<T, R>,
        action: suspend () -> T
    ): R {
        validateInternetConnection()

        return withContext(dispatcher) {
            try {
                val response = action()
                mapper.apply(response)
            } catch (ex: Throwable) {
                crashConsumer.report(ex)
                throw ex
            }
        }
    }

    @Throws(BaseException.NoInternetConnectionException::class)
    private fun validateInternetConnection() {
        if (!networkHandler.hasInternetConnection()) {
            throw BaseException.NoInternetConnectionException
        }
    }
}