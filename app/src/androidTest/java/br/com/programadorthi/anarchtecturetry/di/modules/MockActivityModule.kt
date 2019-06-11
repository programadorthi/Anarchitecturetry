package br.com.programadorthi.anarchtecturetry.di.modules

import br.com.programadorthi.anarchtecturetry.feature.blockchain.presentation.BlockchainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class MockActivityModule {

    @ContributesAndroidInjector
    abstract fun contributeBlockchainActivity(): BlockchainActivity

}