package br.com.programadorthi.base.shared

import br.com.programadorthi.base.exception.BaseException

sealed class LayerResult<out T> {
    data class Failure(val type: FailureType) : LayerResult<Nothing>()
    data class Success<T>(val data: T) : LayerResult<T>()

    companion object {
        @JvmStatic
        fun failure(type: FailureType) = Failure(type)

        @JvmStatic
        fun <T> success(data: T) = Success(data)

        @JvmStatic
        fun fromException(exception: Exception): Failure {
            val type = when (exception) {
                is BaseException.EssentialParamMissingException -> FailureType.EssentialParamsMissing
                is BaseException.HttpCallException -> FailureType.HttpCall(exception.code)
                is BaseException.UnknownEndpointException -> FailureType.BadConfiguration
                else -> FailureType.Unknown
            }
            return failure(type)
        }
    }
}