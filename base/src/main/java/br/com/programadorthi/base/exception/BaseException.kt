package br.com.programadorthi.base.exception

/**
 * Base exception class for handling any exceptions
 */
sealed class BaseException(message: String = "") : RuntimeException(message) {
    /**
     * Exception thrown when an essential parameter is missing in the backend/network response
     */
    class EssentialParamMissingException(
        missingParam: String,
        rawObject: Any
    ) : BaseException("$missingParam are missing in received object: $rawObject")

    /**
     * Data class used to identify a generic http request exception
     */
    data class HttpCallException(val code: Int) :
        BaseException("Server HTTP response code was $code")

    /**
     * Exception to identify an invalid endpoint
     */
    class UnknownEndpointException(url: String) : BaseException("Unknown endpoint: $url")

    /**
     * Exception to identify an invalid endpoint
     */
    class UnexpectedException(exception: Throwable) : BaseException(exception.message ?: "")

    companion object {
        /**
         * Check if the current BaseException is an exception to report.
         * E.g: Send the exception to Crashlytics
         */
        fun isAnExceptionToReport(throwable: Throwable?): Boolean =
            throwable is HttpCallException ||
                    throwable is EssentialParamMissingException ||
                    throwable is UnexpectedException ||
                    throwable is UnknownEndpointException
    }
}