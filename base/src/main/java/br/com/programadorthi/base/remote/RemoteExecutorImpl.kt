package br.com.programadorthi.base.remote

import br.com.programadorthi.base.exception.BaseException
import br.com.programadorthi.base.exception.CrashConsumer
import br.com.programadorthi.base.shared.LayerResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class RemoteExecutorImpl(
    private val crashConsumer: CrashConsumer,
    private val networkHandler: NetworkHandler,
    private val dispatcher: CoroutineDispatcher
) : RemoteExecutor {

    override suspend fun checkConnectionAndThenDone(action: suspend () -> Unit): LayerResult<Boolean> =
        withContext(dispatcher) {
            return@withContext try {
                validateInternetConnection()
                action()
                LayerResult.success(true)
            } catch (ex: Exception) {
                crashConsumer.report(ex)
                LayerResult.failure<Boolean>(ex)
            }
        }

    override suspend fun <T> checkConnectionAndThenReturn(action: suspend () -> T): LayerResult<T> =
        withContext(dispatcher) {
            return@withContext try {
                validateInternetConnection()
                val result = action()
                LayerResult.success(result)
            } catch (ex: Exception) {
                crashConsumer.report(ex)
                LayerResult.failure<T>(ex)
            }
        }

    override suspend fun <T, R> checkConnectionMapperAndThenReturn(
        mapper: BaseRemoteMapper<T, R>,
        action: suspend () -> T
    ): LayerResult<R> = withContext(dispatcher) {
        return@withContext try {
            validateInternetConnection()
            val response = action()
            val mapped = mapper.apply(response)
            LayerResult.success(mapped)
        } catch (ex: Exception) {
            crashConsumer.report(ex)
            LayerResult.failure<R>(ex)
        }
    }

    @Throws(BaseException.NoInternetConnectionException::class)
    private fun validateInternetConnection() {
        if (!networkHandler.hasInternetConnection()) {
            throw BaseException.NoInternetConnectionException
        }
    }
}