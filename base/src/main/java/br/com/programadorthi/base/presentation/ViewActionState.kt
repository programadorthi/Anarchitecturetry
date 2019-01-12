package br.com.programadorthi.base.presentation

/**
 * The current state of view action
 */
sealed class ViewActionState<out T> {
    /**
     * The complete state with a result
     */
    data class Complete<out T>(val result: T) : ViewActionState<T>()

    /**
     * The error state with a error
     */
    data class Error<out T>(val error: Throwable, val data: T) : ViewActionState<T>()

    /**
     * The loading state
     */
    class Loading<out T> : ViewActionState<T>()

    companion object {
        fun <T> complete(result: T): ViewActionState<T> = Complete(result)

        fun <T> failure(error: Throwable, data: T): ViewActionState<T> = Error(error, data)

        fun <T> loading(): ViewActionState<T> = Loading()
    }
}