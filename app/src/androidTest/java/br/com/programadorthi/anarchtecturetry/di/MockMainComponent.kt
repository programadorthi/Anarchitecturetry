package br.com.programadorthi.anarchtecturetry.di

import androidx.lifecycle.ViewModelProvider
import br.com.programadorthi.anarchtecturetry.MockApplication
import br.com.programadorthi.anarchtecturetry.di.modules.MockActivityModule
import br.com.programadorthi.anarchtecturetry.di.modules.MockModule
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidInjectionModule::class,
        MockActivityModule::class,
        MockModule::class
    ]
)
interface MockMainComponent : AndroidInjector<MockApplication> {

    val viewModelFactor: ViewModelProvider.Factory

}