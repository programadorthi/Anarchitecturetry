package br.com.programadorthi.base.model

/**
 * Class used to report an API or Database result
 */
sealed class Resource<out T> {
    class Error<out T>(val error: Throwable, val data: T) : Resource<T>()
    class Success<out T>(val data: T) : Resource<T>()

    companion object {
        fun <T> error(error: Throwable, data: T): Resource<T> = Error(error, data)
        fun <T> success(data: T): Resource<T> = Success(data)
    }
}