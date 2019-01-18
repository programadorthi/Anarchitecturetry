package br.com.programadorthi.anarchtecturetry.di

import br.com.programadorthi.anarchtecturetry.di.modules.DatabaseModule
import br.com.programadorthi.base.di.BaseModule
import br.com.programadorthi.blockchain.di.BlockchainComponent
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        BaseModule::class,
        DatabaseModule::class
    ]
)
interface MainComponent {
    fun blockchainComponent(): BlockchainComponent
}