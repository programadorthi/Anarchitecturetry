package br.com.programadorthi.anarchtecturetry.di.modules

import br.com.programadorthi.anarchtecturetry.feature.blockchain.di.BlockchainModule
import br.com.programadorthi.anarchtecturetry.feature.blockchain.presentation.BlockchainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityModule {

    @ContributesAndroidInjector(modules = [BlockchainModule::class])
    abstract fun contributeBlockchainActivity(): BlockchainActivity

}