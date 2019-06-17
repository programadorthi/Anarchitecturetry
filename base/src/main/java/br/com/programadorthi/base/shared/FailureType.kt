package br.com.programadorthi.base.shared

sealed class FailureType {
    abstract class BusinessFailure : FailureType()
    data class HttpCall(val code: Int) : FailureType()
    object BadConfiguration : FailureType()
    object EssentialParamsMissing : FailureType()
    object NoInternetConnection : FailureType()
    object Unknown : FailureType()
}