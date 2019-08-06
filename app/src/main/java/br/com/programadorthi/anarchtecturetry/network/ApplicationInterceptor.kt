package br.com.programadorthi.anarchtecturetry.network

import br.com.programadorthi.base.exception.BaseException
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.net.UnknownHostException

class ApplicationInterceptor(private val tokenProvider: TokenProvider) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val newRequest = createRequestIfRequired(chain.request())

        val response = try {
            chain.proceed(newRequest)
        } catch (_: UnknownHostException) {
            throw BaseException.UnknownEndpointException(newRequest.url().toString())
        } catch (ex: Exception) {
            throw BaseException.UnexpectedException(ex)
        }

        if (response.isSuccessful) {
            refreshToken(response)
            return response
        }

        throw BaseException.HttpCallException(code = response.code())
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