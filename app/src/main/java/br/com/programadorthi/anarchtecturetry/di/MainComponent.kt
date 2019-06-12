package br.com.programadorthi.anarchtecturetry.di

import android.content.Context
import br.com.programadorthi.anarchtecturetry.di.modules.ApplicationModule
import br.com.programadorthi.anarchtecturetry.di.modules.NetworkModule
import br.com.programadorthi.base.formatter.TextFormatter
import br.com.programadorthi.base.remote.NetworkHandler
import br.com.programadorthi.base.remote.RemoteExecutor
import br.com.programadorthi.base.utils.ANDROID_SCHEDULER
import br.com.programadorthi.base.utils.DATE_FORMATTER
import br.com.programadorthi.base.utils.IO_SCHEDULER
import br.com.programadorthi.base.utils.MONEY_FORMATTER
import com.squareup.moshi.Moshi
import dagger.BindsInstance
import dagger.Component
import io.reactivex.Scheduler
import io.reactivex.functions.Consumer
import retrofit2.Retrofit
import java.math.BigDecimal
import java.util.*
import javax.inject.Named
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        ApplicationModule::class,
        NetworkModule::class
    ]
)
interface MainComponent {

    @Named(ANDROID_SCHEDULER)
    fun androidScheduler(): Scheduler

    @Named(IO_SCHEDULER)
    fun ioScheduler(): Scheduler

    @Named(DATE_FORMATTER)
    fun dateFormatter(): TextFormatter<Date>

    @Named(MONEY_FORMATTER)
    fun moneyFormatter(): TextFormatter<BigDecimal>

    fun context(): Context

    fun crashlyticsConsumer(): Consumer<Throwable>

    fun networkHandler(): NetworkHandler

    fun remoteExecutor(): RemoteExecutor

    fun moshi(): Moshi

    fun retrofit(): Retrofit

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun context(context: Context): Builder

        fun build(): MainComponent
    }
}