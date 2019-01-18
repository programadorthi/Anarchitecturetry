package br.com.programadorthi.blockchain.di

import br.com.programadorthi.base.di.FeatureScope
import br.com.programadorthi.blockchain.presentation.BlockchainActivity
import dagger.Subcomponent

@FeatureScope
@Subcomponent(modules = [BlockchainModule::class])
interface BlockchainComponent {

    fun inject(activity: BlockchainActivity)

}