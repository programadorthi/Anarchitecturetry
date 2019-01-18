package br.com.programadorthi.anarchtecturetry.di

import android.content.Context
import br.com.programadorthi.anarchtecturetry.MainApplication
import br.com.programadorthi.anarchtecturetry.di.modules.ApplicationModule
import br.com.programadorthi.anarchtecturetry.di.modules.DatabaseModule
import br.com.programadorthi.blockchain.di.BlockchainActivityModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidInjectionModule::class,
        ApplicationModule::class,
        BlockchainActivityModule::class,
        DatabaseModule::class
    ]
)
interface MainComponent {

    fun inject(application: MainApplication)

    interface Builder {

        @BindsInstance
        fun applicationContext(context: Context): Builder

        fun build(): MainComponent

    }

}