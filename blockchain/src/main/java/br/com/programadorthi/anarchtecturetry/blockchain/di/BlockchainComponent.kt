package br.com.programadorthi.anarchtecturetry.blockchain.di

import br.com.programadorthi.anarchtecturetry.blockchain.presentation.BlockchainActivity
import br.com.programadorthi.anarchtecturetry.di.BaseActivityComponent
import br.com.programadorthi.anarchtecturetry.di.MainComponent
import br.com.programadorthi.anarchtecturetry.di.scopes.FeatureScope
import dagger.Component

@Component(
    modules = [BlockchainModule::class],
    dependencies = [MainComponent::class]
)
@FeatureScope
interface BlockchainComponent : BaseActivityComponent<BlockchainActivity> {
    @Component.Builder
    interface Builder {
        fun build(): BlockchainComponent
        fun mainComponent(mainComponent: MainComponent): Builder
    }
}