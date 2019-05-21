package br.com.programadorthi.anarchtecturetry.di.modules

import android.content.Context
import br.com.programadorthi.anarchtecturetry.firebase.CrashlyticsConsumer
import br.com.programadorthi.base.formatter.DateFormatter
import br.com.programadorthi.base.formatter.MoneyFormatter
import br.com.programadorthi.base.adapter.BigDecimalJsonAdapter
import br.com.programadorthi.base.remote.NetworkHandler
import br.com.programadorthi.base.remote.NetworkHandlerImpl
import br.com.programadorthi.base.remote.RemoteExecutor
import br.com.programadorthi.base.remote.RemoteExecutorImpl
import br.com.programadorthi.base.presentation.TextFormatter
import br.com.programadorthi.base.utils.ANDROID_SCHEDULER
import br.com.programadorthi.base.utils.DATE_FORMATTER
import br.com.programadorthi.base.utils.IO_SCHEDULER
import br.com.programadorthi.base.utils.MONEY_FORMATTER
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import io.reactivex.Scheduler
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import java.math.BigDecimal
import java.util.*
import javax.inject.Named
import javax.inject.Singleton

@Module
object ApplicationModule {

    @Provides
    @Named(ANDROID_SCHEDULER)
    @JvmStatic
    fun provideAndroidScheduler(): Scheduler = Schedulers.computation()

    @Provides
    @Named(IO_SCHEDULER)
    @JvmStatic
    fun provideIOScheduler(): Scheduler = Schedulers.io()

    @Provides
    @Named(DATE_FORMATTER)
    @JvmStatic
    fun provideDateFormatter(): TextFormatter<Date> = DateFormatter()

    @Provides
    @Named(MONEY_FORMATTER)
    @JvmStatic
    fun provideMoneyFormatter(): TextFormatter<BigDecimal> = MoneyFormatter()

    @Provides
    @JvmStatic
    fun provideCrashlyticsConsumer(): Consumer<Throwable> = CrashlyticsConsumer()

    @Provides
    @JvmStatic
    fun provideNetworkHandler(context: Context): NetworkHandler = NetworkHandlerImpl(context)

    @Provides
    @JvmStatic
    fun provideRemoteExecutor(
        crashConsumer: Consumer<Throwable>,
        networkHandler: NetworkHandler,
        @Named(IO_SCHEDULER) scheduler: Scheduler
    ): RemoteExecutor = RemoteExecutorImpl(crashConsumer, networkHandler, scheduler)

    @Provides
    @Singleton
    @JvmStatic
    fun provideMoshi(): Moshi = Moshi.Builder()
        .add(BigDecimal::class.java, BigDecimalJsonAdapter())
        .build()
}