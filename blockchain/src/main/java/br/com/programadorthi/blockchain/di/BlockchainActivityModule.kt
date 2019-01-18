package br.com.programadorthi.blockchain.di

import br.com.programadorthi.base.di.FeatureScope
import br.com.programadorthi.blockchain.presentation.BlockchainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class BlockchainActivityModule {

    @FeatureScope
    @ContributesAndroidInjector(modules = [BlockchainModule::class])
    abstract fun contributeBlockchainActivityInjector(): BlockchainActivity

}