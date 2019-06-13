package br.com.programadorthi.anarchtecturetry.network

import br.com.programadorthi.base.exception.BaseException
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import timber.log.Timber
import java.net.HttpURLConnection
import java.net.UnknownHostException

class ApplicationInterceptor(private val tokenProvider: TokenProvider) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val newRequest = createRequestIfRequired(chain.request())

        val response = try {
            chain.proceed(newRequest)
        } catch (_: UnknownHostException) {
            throw BaseException.UnknownEndpointException(newRequest.url().toString())
        }

        if (response.isSuccessful) {
            refreshToken(response)
            return response
        }

        when (response.code()) {
            HttpURLConnection.HTTP_BAD_REQUEST -> {
                // Capture your backend validation and replace the call below
                throw BaseException.HttpCallException(code = response.code())
            }
            HttpURLConnection.HTTP_UNAUTHORIZED -> {
                throw BaseException.UnauthorizedException
            }
            else -> throw BaseException.HttpCallException(code = response.code())
        }
    }

    private fun refreshToken(response: Response?) {
        val resp = response ?: return
        val token = resp.header(AUTHORIZATION_PROPERTY)
        if (token.isNullOrBlank()) return
        tokenProvider.saveToken(token)
    }

    private fun createRequestIfRequired(request: Request): Request {
        val noAuthorizationHeader = request.header(NO_AUTHORIZATION_PROPERTY)
        if (noAuthorizationHeader.isNullOrBlank()) {
            return request.newBuilder()
                .addHeader(AUTHORIZATION_PROPERTY, tokenProvider.getToken())
                .build()
        }
        return request
    }

    companion object {
        private const val AUTHORIZATION_PROPERTY = "Authorization"
        private const val NO_AUTHORIZATION_PROPERTY = "No-Authorization"
        const val NO_AUTHORIZATION_HEADER = "$NO_AUTHORIZATION_PROPERTY: true"
    }
}