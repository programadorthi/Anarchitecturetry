package br.com.programadorthi.anarchtecturetry.di.modules

import br.com.programadorthi.anarchtecturetry.BuildConfig
import br.com.programadorthi.anarchtecturetry.network.ApplicationInterceptor
import br.com.programadorthi.anarchtecturetry.network.TokenProvider
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
object NetworkModule {

    private const val APPLICATION_INTERCEPTOR = "APPLICATION_INTERCEPTOR"
    private const val HTTP_LOGGING_INTERCEPTOR = "HTTP_LOGGING_INTERCEPTOR"

    @Provides
    @Singleton
    @JvmStatic
    fun provideConverter(moshi: Moshi): Converter.Factory = MoshiConverterFactory.create(moshi)

    @Provides
    @Singleton
    @Named(APPLICATION_INTERCEPTOR)
    @JvmStatic
    fun provideApplicationInterceptor(tokenProvider: TokenProvider): Interceptor {
        return ApplicationInterceptor(tokenProvider)
    }

    @Provides
    @Singleton
    @Named(HTTP_LOGGING_INTERCEPTOR)
    @JvmStatic
    fun provideHttpLoggingInterceptor(): Interceptor {
        return HttpLoggingInterceptor(
            HttpLoggingInterceptor.Logger { message -> Timber.d(message); }
        ).apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    @Provides
    @Singleton
    @JvmStatic
    fun provideOkHttpClient(
        @Named(APPLICATION_INTERCEPTOR) applicationInterceptor: Interceptor,
        @Named(HTTP_LOGGING_INTERCEPTOR) httpLoggingInterceptor: Interceptor
    ): OkHttpClient {

        val okHttpClientBuilder = OkHttpClient.Builder()
        okHttpClientBuilder.readTimeout(120, TimeUnit.SECONDS)
        okHttpClientBuilder.connectTimeout(120, TimeUnit.SECONDS)

        okHttpClientBuilder.addInterceptor(applicationInterceptor)

        // Order is important. Always put the log interceptor last
        if (BuildConfig.DEBUG) {
            okHttpClientBuilder.addInterceptor(httpLoggingInterceptor)
        }

        return okHttpClientBuilder.build()
    }

    @Provides
    @Singleton
    @JvmStatic
    fun provideRetrofit(httpClient: OkHttpClient, converter: Converter.Factory): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.API_BASE_URL)
            .addConverterFactory(converter)
            .client(httpClient)
            .build()
    }
}