package br.com.programadorthi.anarchtecturetry.blockchain.di

import br.com.programadorthi.anarchtecturetry.blockchain.presentation.BlockchainViewModel
import br.com.programadorthi.anarchtecturetry.di.MainComponent
import br.com.programadorthi.anarchtecturetry.di.scopes.FeatureScope
import dagger.Component

@Component(
    modules = [BlockchainModule::class],
    dependencies = [MainComponent::class]
)
@FeatureScope
interface BlockchainComponent {
    fun blockchainViewModel(): BlockchainViewModel

    @Component.Factory
    interface Factory {
        fun blockchainComponent(mainComponent: MainComponent): BlockchainComponent
    }
}