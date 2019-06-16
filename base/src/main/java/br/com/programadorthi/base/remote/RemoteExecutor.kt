package br.com.programadorthi.base.remote

import br.com.programadorthi.base.exception.BaseException

interface RemoteExecutor {

    @Throws(BaseException::class)
    suspend fun checkConnectionAndThenDone(action: suspend () -> Unit)

    @Throws(BaseException::class)
    suspend fun <T> checkConnectionAndThenReturn(action: suspend () -> T): T

    @Throws(BaseException::class)
    suspend fun <T, R> checkConnectionMapperAndThenReturn(mapper: BaseRemoteMapper<T, R>, action: suspend () -> T): R

}