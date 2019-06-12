@file:JvmName("BlockchainInjector")

package br.com.programadorthi.anarchtecturetry.blockchain.di

import br.com.programadorthi.anarchtecturetry.blockchain.presentation.BlockchainActivity
import br.com.programadorthi.anarchtecturetry.extension.mainComponent

fun BlockchainActivity.inject() {
    DaggerBlockchainComponent.builder()
        .mainComponent(this.mainComponent())
        .build()
        .inject(this)
}