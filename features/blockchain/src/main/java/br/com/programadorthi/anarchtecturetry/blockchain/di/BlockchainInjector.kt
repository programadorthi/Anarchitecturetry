package br.com.programadorthi.anarchtecturetry.blockchain.di

import br.com.programadorthi.anarchtecturetry.blockchain.di.modules.BlockchainPresentationModule
import br.com.programadorthi.anarchtecturetry.blockchain.presentation.BlockchainActivity
import br.com.programadorthi.anarchtecturetry.di.mainComponent

fun BlockchainActivity.inject() {
    DaggerBlockchainComponent.factory()
        .blockchainComponent(BlockchainPresentationModule(this), this.mainComponent())
        .inject(this)
}