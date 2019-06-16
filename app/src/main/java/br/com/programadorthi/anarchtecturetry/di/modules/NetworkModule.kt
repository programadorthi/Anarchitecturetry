package br.com.programadorthi.anarchtecturetry.di.modules

import br.com.programadorthi.anarchtecturetry.BuildConfig
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

    private const val HTTP_LOGGING_INTERCEPTOR = "http_logging_interceptor"

    @Provides
    @Singleton
    @JvmStatic
    fun provideConverter(moshi: Moshi): Converter.Factory = MoshiConverterFactory.create(moshi)

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
        @Named(HTTP_LOGGING_INTERCEPTOR) httpLoggingInterceptor: Interceptor
    ): OkHttpClient {

        val okHttpClientBuilder = OkHttpClient.Builder()
        okHttpClientBuilder.readTimeout(120, TimeUnit.SECONDS)
        okHttpClientBuilder.connectTimeout(120, TimeUnit.SECONDS)

        if (BuildConfig.DEBUG) {
            okHttpClientBuilder.addInterceptor(httpLoggingInterceptor)
        }

        return okHttpClientBuilder.build()
    }

    @Provides
    @Singleton
    @JvmStatic
    fun provideRetrofit(
        httpClient: OkHttpClient,
        converter: Converter.Factory
    ): Retrofit {

        return Retrofit.Builder()
            .baseUrl(BuildConfig.API_BASE_URL)
            .addConverterFactory(converter)
            .client(httpClient)
            .build()

    }
}