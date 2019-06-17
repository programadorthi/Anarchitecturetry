package br.com.programadorthi.base.presentation

/**
 * The current state of view action
 */
sealed class ViewState<T> {
    /**
     * The complete state with a data
     */
    data class Complete<T>(val result: T) : ViewState<T>()

    /**
     * The error state with a error
     */
    data class Error<T>(val error: Throwable) : ViewState<T>()

    /**
     * The loading state
     */
    class Loading<T> : ViewState<T>()
}