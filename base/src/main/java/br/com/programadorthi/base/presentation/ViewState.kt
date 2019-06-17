package br.com.programadorthi.base.presentation

import br.com.programadorthi.base.shared.FailureType

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
    data class Failure<T>(val type: FailureType) : ViewState<T>()

    /**
     * The loading state
     */
    class Loading<T> : ViewState<T>()
}