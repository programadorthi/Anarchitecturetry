package br.com.programadorthi.base.exception

/**
 * Base exception class for handling any exceptions
 */
sealed class BaseException(message: String = "") : RuntimeException(message) {
    /**
     * Base class used for specific feature failures
     */
    abstract class BusinessException : BaseException()

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
     * Object used to identify a network without internet connection
     */
    object NoInternetConnectionException : BaseException()

    /**
     * Object used to identify an authorized user. E.g: HTTP 401
     */
    object UnauthorizedException : BaseException()

    /**
     * Exception to identify an invalid endpoint
     */
    class UnknownEndpointException(url: String) : BaseException("Unknown endpoint: $url")

    companion object {
        /**
         * Check if the current BaseException is an exception to report.
         * E.g: Send the exception to Crashlytics
         */
        fun isAnExceptionToReport(throwable: Throwable?): Boolean =
            throwable is HttpCallException ||
                    throwable is EssentialParamMissingException ||
                    throwable is UnknownEndpointException
    }
}