package br.com.programadorthi.base.remote

import br.com.programadorthi.base.exception.CrashConsumer
import br.com.programadorthi.base.shared.FailureType
import br.com.programadorthi.base.shared.LayerResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class RemoteExecutorImpl(
    private val crashConsumer: CrashConsumer,
    private val networkHandler: NetworkHandler,
    private val dispatcher: CoroutineDispatcher
) : RemoteExecutor {

    override suspend fun checkConnectionAndThenDone(action: suspend () -> Unit): LayerResult<Boolean> =
        execute {
            action()
            LayerResult.success(true)
        }

    override suspend fun <T> checkConnectionAndThenReturn(action: suspend () -> T): LayerResult<T> =
        execute {
            val result = action()
            LayerResult.success(result)
        }

    override suspend fun <T, R> checkConnectionMapperAndThenReturn(
        mapper: BaseRemoteMapper<T, R>,
        action: suspend () -> T
    ): LayerResult<R> = execute {
        val response = action()
        val mapped = mapper.apply(response)
        LayerResult.success(mapped)
    }

    private suspend fun <T> execute(body: suspend () -> LayerResult<T>): LayerResult<T> = withContext(dispatcher) {
        if (!networkHandler.hasInternetConnection()) {
            return@withContext LayerResult.failure(FailureType.NoInternetConnection)
        }

        return@withContext try {
            body()
        } catch (ex: Exception) {
            crashConsumer.report(ex)
            LayerResult.fromException(ex)
        }
    }
}