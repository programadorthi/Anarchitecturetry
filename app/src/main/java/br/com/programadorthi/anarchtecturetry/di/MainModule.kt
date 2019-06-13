package br.com.programadorthi.anarchtecturetry.di

import br.com.programadorthi.anarchtecturetry.BuildConfig
import br.com.programadorthi.anarchtecturetry.crashlytics.CrashlyticsConsumer
import br.com.programadorthi.base.adapter.BigDecimalJsonAdapter
import br.com.programadorthi.base.formatter.DateFormatter
import br.com.programadorthi.base.formatter.MoneyFormatter
import br.com.programadorthi.base.formatter.TextFormatter
import br.com.programadorthi.base.remote.NetworkHandler
import br.com.programadorthi.base.remote.NetworkHandlerImpl
import br.com.programadorthi.base.remote.RemoteExecutor
import br.com.programadorthi.base.remote.RemoteExecutorImpl
import br.com.programadorthi.base.utils.ANDROID_SCHEDULER
import br.com.programadorthi.base.utils.DATE_FORMATTER
import br.com.programadorthi.base.utils.IO_SCHEDULER
import br.com.programadorthi.base.utils.MONEY_FORMATTER
import com.squareup.moshi.Moshi
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import timber.log.Timber
import java.math.BigDecimal
import java.util.*
import java.util.concurrent.TimeUnit

private const val HTTP_LOGGING_INTERCEPTOR = "http_logging_interceptor"

val mainModule = module {

    // =================================================
    // ============ SHARED CONFIGURATIONS ==============
    // =================================================

    factory(named(name = ANDROID_SCHEDULER)) { Schedulers.computation() }

    factory(named(name = IO_SCHEDULER)) { Schedulers.io() }

    factory<TextFormatter<Date>>(named(name = DATE_FORMATTER)) { DateFormatter() }

    factory<TextFormatter<BigDecimal>>(named(name = MONEY_FORMATTER)) { MoneyFormatter() }

    factory<Consumer<Throwable>> { CrashlyticsConsumer() }

    factory<NetworkHandler> { NetworkHandlerImpl(context = get()) }

    factory<RemoteExecutor> {
        RemoteExecutorImpl(
            crashConsumer = get(), networkHandler = get(), scheduler = get(named(IO_SCHEDULER))
        )
    }

    single {
        Moshi.Builder()
            .add(BigDecimal::class.java, BigDecimalJsonAdapter())
            .build()
    }

    // =================================================
    // ============ NETWORK CONFIGURATIONS =============
    // =================================================

    single<Converter.Factory> { MoshiConverterFactory.create(get()) }

    single<Interceptor>(named(name = HTTP_LOGGING_INTERCEPTOR)) {
        HttpLoggingInterceptor(
            HttpLoggingInterceptor.Logger { message -> Timber.d(message) }
        ).apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    single<OkHttpClient> {
        val okHttpClientBuilder = OkHttpClient.Builder()
        okHttpClientBuilder.readTimeout(120, TimeUnit.SECONDS)
        okHttpClientBuilder.connectTimeout(120, TimeUnit.SECONDS)
        if (BuildConfig.DEBUG) {
            okHttpClientBuilder.addInterceptor(get(named(name = HTTP_LOGGING_INTERCEPTOR)))
        }

        //okHttpClientBuilder.addInterceptor(auth)

        return@single okHttpClientBuilder.build()
    }

    single<Retrofit> {
        Retrofit.Builder()
            .baseUrl(BuildConfig.API_BASE_URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(get())
            .client(get())
            .build()
    }
}