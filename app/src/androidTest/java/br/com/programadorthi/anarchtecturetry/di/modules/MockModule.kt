package br.com.programadorthi.anarchtecturetry.di.modules

import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import io.mockk.mockk
import javax.inject.Singleton

@Module
object MockModule {

    @Provides
    @Singleton
    @JvmStatic
    fun provideViewModelFactory(): ViewModelProvider.Factory = mockk()

}