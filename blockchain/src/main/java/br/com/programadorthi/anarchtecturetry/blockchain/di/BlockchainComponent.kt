package br.com.programadorthi.anarchtecturetry.blockchain.di

import br.com.programadorthi.anarchtecturetry.blockchain.di.modules.BlockchainModule
import br.com.programadorthi.anarchtecturetry.blockchain.di.modules.BlockchainPresentationModule
import br.com.programadorthi.anarchtecturetry.blockchain.presentation.BlockchainActivity
import br.com.programadorthi.anarchtecturetry.di.BaseActivityComponent
import br.com.programadorthi.anarchtecturetry.di.MainComponent
import br.com.programadorthi.anarchtecturetry.di.scopes.FeatureScope
import dagger.Component

@Component(
    modules = [
        BlockchainModule::class,
        BlockchainPresentationModule::class
    ],
    dependencies = [MainComponent::class]
)
@FeatureScope
interface BlockchainComponent : BaseActivityComponent<BlockchainActivity> {
    @Component.Factory
    interface Factory {
        fun blockchainComponent(
            blockchainPresentationModule: BlockchainPresentationModule,
            mainComponent: MainComponent
        ): BlockchainComponent
    }
}