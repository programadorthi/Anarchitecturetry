package br.com.programadorthi.anarchtecturetry.di

import android.content.Context
import br.com.programadorthi.anarchtecturetry.MainApplication
import br.com.programadorthi.anarchtecturetry.di.modules.ActivityModule
import br.com.programadorthi.anarchtecturetry.di.modules.ApplicationModule
import br.com.programadorthi.anarchtecturetry.di.modules.DatabaseModule
import br.com.programadorthi.anarchtecturetry.di.modules.NetworkModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        ActivityModule::class,
        AndroidInjectionModule::class,
        ApplicationModule::class,
        DatabaseModule::class,
        NetworkModule::class
    ]
)
interface MainComponent : AndroidInjector<MainApplication> {

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun context(context: Context): Builder

        fun build(): MainComponent
    }
}