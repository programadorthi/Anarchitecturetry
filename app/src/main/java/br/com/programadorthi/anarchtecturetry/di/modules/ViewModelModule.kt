package br.com.programadorthi.anarchtecturetry.di.modules

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import br.com.programadorthi.anarchtecturetry.di.viewmodel.ViewModelFactory
import br.com.programadorthi.anarchtecturetry.di.viewmodel.ViewModelKey
import br.com.programadorthi.blockchain.presentation.BlockchainViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @Binds
    abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(BlockchainViewModel::class)
    abstract fun bindBlockchainViewModel(blockchainViewModel: BlockchainViewModel): ViewModel

}