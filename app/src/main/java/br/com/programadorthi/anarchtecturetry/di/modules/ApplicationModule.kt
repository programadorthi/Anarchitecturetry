package br.com.programadorthi.anarchtecturetry.di.modules

import android.content.Context
import br.com.programadorthi.anarchtecturetry.firebase.CrashlyticsConsumer
import br.com.programadorthi.base.adapter.BigDecimalJsonAdapter
import br.com.programadorthi.base.exception.CrashConsumer
import br.com.programadorthi.base.formatter.DateTimeFormatter
import br.com.programadorthi.base.formatter.MoneyFormatter
import br.com.programadorthi.base.formatter.TextFormatter
import br.com.programadorthi.base.remote.NetworkHandler
import br.com.programadorthi.base.remote.NetworkHandlerImpl
import br.com.programadorthi.base.remote.RemoteExecutor
import br.com.programadorthi.base.remote.RemoteExecutorImpl
import br.com.programadorthi.base.utils.ANDROID_DISPATCHER
import br.com.programadorthi.base.utils.DATE_FORMATTER
import br.com.programadorthi.base.utils.IO_DISPATCHER
import br.com.programadorthi.base.utils.MONEY_FORMATTER
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import java.math.BigDecimal
import java.util.*
import javax.inject.Named
import javax.inject.Singleton

@Module
object ApplicationModule {

    @Provides
    @Named(ANDROID_DISPATCHER)
    @JvmStatic
    fun provideAndroidDispatcher(): CoroutineDispatcher = Dispatchers.Main

    @Provides
    @Named(IO_DISPATCHER)
    @JvmStatic
    fun provideIODispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Provides
    @Named(DATE_FORMATTER)
    @JvmStatic
    fun provideDateTimeFormatter(): TextFormatter<Date> = DateTimeFormatter()

    @Provides
    @Named(MONEY_FORMATTER)
    @JvmStatic
    fun provideMoneyFormatter(): TextFormatter<BigDecimal> = MoneyFormatter()

    @Provides
    @JvmStatic
    fun provideCoroutineScope(@Named(IO_DISPATCHER) dispatcher: CoroutineDispatcher): CoroutineScope =
        CoroutineScope(dispatcher + Job())

    @Provides
    @JvmStatic
    fun provideCrashlyticsConsumer(): CrashConsumer = CrashlyticsConsumer()

    @Provides
    @JvmStatic
    fun provideNetworkHandler(context: Context): NetworkHandler = NetworkHandlerImpl(context)

    @Provides
    @JvmStatic
    fun provideRemoteExecutor(
        crashConsumer: CrashConsumer,
        networkHandler: NetworkHandler,
        @Named(IO_DISPATCHER) dispatcher: CoroutineDispatcher
    ): RemoteExecutor = RemoteExecutorImpl(crashConsumer, networkHandler, dispatcher)

    @Provides
    @Singleton
    @JvmStatic
    fun provideMoshi(): Moshi = Moshi.Builder()
        .add(BigDecimal::class.java, BigDecimalJsonAdapter())
        .build()
}