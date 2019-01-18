package br.com.programadorthi.base.di

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import br.com.programadorthi.base.adapter.BigDecimalJsonAdapter
import br.com.programadorthi.base.exception.CrashlyticsConsumer
import br.com.programadorthi.base.network.NetworkHandler
import br.com.programadorthi.base.network.NetworkHandlerImpl
import br.com.programadorthi.base.network.RemoteExecutor
import br.com.programadorthi.base.network.RemoteExecutorImpl
import br.com.programadorthi.base.utils.Constants
import com.squareup.moshi.JsonAdapter
import dagger.Module
import dagger.Provides
import io.reactivex.Scheduler
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import java.math.BigDecimal
import javax.inject.Named
import javax.inject.Provider
import javax.inject.Singleton

@Module
object BaseModule {

    @Provides
    @JvmStatic
    fun provideBigDecimalJsonAdapter(): JsonAdapter<BigDecimal> = BigDecimalJsonAdapter()

    @Provides
    @Singleton
    @JvmStatic
    fun provideCrashlyticsConsumer(): Consumer<Throwable> = CrashlyticsConsumer()

    @Provides
    @Singleton
    @Named(Constants.COMPUTATION_SCHEDULER)
    @JvmStatic
    fun provideComputationScheduler(): Scheduler = Schedulers.computation()

    @Provides
    @Singleton
    @Named(Constants.IO_SCHEDULER)
    @JvmStatic
    fun provideIOScheduler(): Scheduler = Schedulers.io()

    @Provides
    @Singleton
    @JvmStatic
    fun provideNetworkHandler(context: Context): NetworkHandler = NetworkHandlerImpl(context)

    @Provides
    @Singleton
    @JvmStatic
    fun provideRemoteExecutor(
        crashlyticsConsumer: Consumer<Throwable>,
        networkHandler: NetworkHandler,
        @Named(Constants.IO_SCHEDULER) scheduler: Scheduler
    ): RemoteExecutor = RemoteExecutorImpl(crashlyticsConsumer, networkHandler, scheduler)

    @Provides
    @Singleton
    @JvmStatic
    fun provideViewModelFactory(
        creators: Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>
    ): ViewModelProvider.Factory = ViewModelFactory(creators)

}