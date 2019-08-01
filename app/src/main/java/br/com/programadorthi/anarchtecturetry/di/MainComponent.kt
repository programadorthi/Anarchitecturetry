package br.com.programadorthi.anarchtecturetry.di

import android.content.Context
import br.com.programadorthi.anarchtecturetry.di.modules.MainModule
import br.com.programadorthi.anarchtecturetry.di.modules.NetworkModule
import br.com.programadorthi.base.exception.CrashConsumer
import br.com.programadorthi.base.formatter.TextFormatter
import br.com.programadorthi.base.remote.RemoteExecutor
import br.com.programadorthi.base.utils.COMPUTATION_DISPATCHER
import br.com.programadorthi.base.utils.DATE_FORMATTER
import br.com.programadorthi.base.utils.IO_DISPATCHER
import br.com.programadorthi.base.utils.MONEY_FORMATTER
import dagger.BindsInstance
import dagger.Component
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import retrofit2.Retrofit
import java.math.BigDecimal
import java.util.*
import javax.inject.Named
import javax.inject.Singleton

@Component(
    modules = [
        MainModule::class,
        NetworkModule::class
    ]
)
@Singleton
interface MainComponent {

    fun context(): Context

    fun computationScope(): CoroutineScope

    fun crashlyticsConsumer(): CrashConsumer

    fun remoteExecutor(): RemoteExecutor

    fun retrofit(): Retrofit

    @Named(COMPUTATION_DISPATCHER)
    fun computationDispatcher(): CoroutineDispatcher

    @Named(IO_DISPATCHER)
    fun ioDispatcher(): CoroutineDispatcher

    @Named(DATE_FORMATTER)
    fun dateTimeFormatter(): TextFormatter<Date>

    @Named(MONEY_FORMATTER)
    fun moneyFormatter(): TextFormatter<BigDecimal>

    @Component.Factory
    interface Factory {
        fun mainComponent(@BindsInstance applicationContext: Context): MainComponent
    }

}