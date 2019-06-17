package br.com.programadorthi.base.shared

sealed class LayerResult<out T> {
    data class Failure<T>(val exception: Throwable) : LayerResult<T>()
    data class Success<T>(val data: T) : LayerResult<T>()

    companion object {
        @JvmStatic
        fun <T> failure(exception: Throwable) = Failure<T>(exception)

        @JvmStatic
        fun <T> success(data: T) = Success(data)
    }
}