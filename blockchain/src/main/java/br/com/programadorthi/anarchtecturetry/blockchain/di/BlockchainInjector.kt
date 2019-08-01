package br.com.programadorthi.anarchtecturetry.blockchain.di

import br.com.programadorthi.anarchtecturetry.blockchain.presentation.BlockchainActivity
import br.com.programadorthi.anarchtecturetry.di.BaseActivityInjector
import br.com.programadorthi.anarchtecturetry.di.mainComponent

object BlockchainInjector : BaseActivityInjector<BlockchainActivity, BlockchainComponent> {
    override fun inject(target: BlockchainActivity): BlockchainComponent {
        return DaggerBlockchainComponent.factory()
            .blockchainComponent(target.mainComponent())
    }
}