package br.com.programadorthi.anarchtecturetry.di.modules

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import br.com.programadorthi.anarchtecturetry.di.annotations.ViewModelKey
import br.com.programadorthi.anarchtecturetry.feature.blockchain.presentation.BlockchainViewModel
import br.com.programadorthi.anarchtecturetry.viewmodel.ViewModelFactory
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(BlockchainViewModel::class)
    abstract fun provideBlockchainViewModel(blockchainViewModel: BlockchainViewModel): ViewModel
}