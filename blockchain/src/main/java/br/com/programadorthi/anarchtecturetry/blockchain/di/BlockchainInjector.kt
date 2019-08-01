package br.com.programadorthi.anarchtecturetry.blockchain.di

import br.com.programadorthi.anarchtecturetry.blockchain.di.modules.BlockchainPresentationModule
import br.com.programadorthi.anarchtecturetry.blockchain.presentation.BlockchainActivity
import br.com.programadorthi.anarchtecturetry.di.mainComponent

fun inject(target: BlockchainActivity) {
    DaggerBlockchainComponent.factory()
        .blockchainComponent(BlockchainPresentationModule(target), target.mainComponent())
        .inject(target)
}