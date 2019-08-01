package br.com.programadorthi.base.remote

import br.com.programadorthi.base.shared.LayerResult

/**
 * Implements this interface to generate commons API request behaviors
 */
interface RemoteExecutor {

    /**
     * Check there is connection before do an API request that doesn't returns nothing
     *
     * @param action A suspend function with API request
     * @return A data that says it was completed or no
     */
    suspend fun checkConnectionAndThenDone(action: suspend () -> Unit): LayerResult<Boolean>

    /**
     * Check there is connection before do an API request that returns a value
     *
     * @param action A suspend function with API request that returns the value
     * @return A [LayerResult] with the execution state of API request
     */
    suspend fun <T> checkConnectionAndThenReturn(action: suspend () -> T): LayerResult<T>

    /**
     * Check there is connection before do an API request that returns a mapped value
     *
     * @param mapper A [BaseRemoteMapper] implementation to mapper the API request value an a specific value
     * @param action A suspend function with API request that returns the value
     * @return A [LayerResult] with a new value mapped from API request value
     */
    suspend fun <T, R> checkConnectionMapperAndThenReturn(
        mapper: BaseRemoteMapper<T, R>,
        action: suspend () -> T
    ): LayerResult<R>

}