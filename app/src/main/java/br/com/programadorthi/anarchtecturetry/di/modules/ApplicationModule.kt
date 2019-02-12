package br.com.programadorthi.anarchtecturetry.di.modules

import android.content.Context
import br.com.programadorthi.base.adapter.BigDecimalJsonAdapter
import br.com.programadorthi.base.exception.CrashlyticsConsumer
import br.com.programadorthi.base.network.NetworkHandler
import br.com.programadorthi.base.network.NetworkHandlerImpl
import br.com.programadorthi.base.network.RemoteExecutor
import br.com.programadorthi.base.network.RemoteExecutorImpl
import br.com.programadorthi.base.utils.Constants
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import io.reactivex.Scheduler
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import java.math.BigDecimal
import javax.inject.Named
import javax.inject.Singleton

@Module
object ApplicationModule {

    @Provides
    @Named(Constants.ANDROID_SCHEDULER)
    @JvmStatic
    fun provideAndroidScheduler(): Scheduler = Schedulers.computation()

    @Provides
    @Named(Constants.IO_SCHEDULER)
    @JvmStatic
    fun provideIOScheduler(): Scheduler = Schedulers.io()

    @Provides
    @JvmStatic
    fun provideCrashlyticsConsumer(): Consumer<Throwable> = CrashlyticsConsumer()

    @Provides
    @JvmStatic
    fun provideNetworkHandler(context: Context): NetworkHandler = NetworkHandlerImpl(context)

    @Provides
    @JvmStatic
    fun provideRemoteExecutor(
        crashlyticsConsumer: Consumer<Throwable>,
        networkHandler: NetworkHandler,
        @Named(Constants.IO_SCHEDULER) scheduler: Scheduler
    ): RemoteExecutor = RemoteExecutorImpl(crashlyticsConsumer, networkHandler, scheduler)

    @Provides
    @Singleton
    @JvmStatic
    fun provideMoshi(): Moshi = Moshi.Builder()
        .add(BigDecimal::class.java, BigDecimalJsonAdapter())
        .build()
}