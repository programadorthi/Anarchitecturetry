package br.com.programadorthi.blockchain.di

import androidx.lifecycle.ViewModel
import br.com.programadorthi.base.di.ViewModelKey
import br.com.programadorthi.base.network.BaseMapper
import br.com.programadorthi.base.network.RemoteExecutor
import br.com.programadorthi.base.utils.Constants
import br.com.programadorthi.base.viewmodel.ViewModelHelper
import br.com.programadorthi.blockchain.data.BlockchainRepositoryImpl
import br.com.programadorthi.blockchain.data.local.BlockchainDao
import br.com.programadorthi.blockchain.data.local.BlockchainLocalRepository
import br.com.programadorthi.blockchain.data.local.BlockchainLocalRepositoryImpl
import br.com.programadorthi.blockchain.data.remote.*
import br.com.programadorthi.blockchain.domain.Blockchain
import br.com.programadorthi.blockchain.domain.BlockchainInteractor
import br.com.programadorthi.blockchain.domain.BlockchainInteractorImpl
import br.com.programadorthi.blockchain.domain.BlockchainRepository
import br.com.programadorthi.blockchain.presentation.BlockchainViewModel
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import io.reactivex.Scheduler
import retrofit2.Retrofit
import javax.inject.Named
import javax.inject.Singleton

@Module
object BlockchainModule {

    // YOU CAN'T PUT THE DATABASE INJECTIONS HERE
    // BECAUSE YOU DON'T KNOW THE ROOM DATABASE IMPLEMENTATION

    //==============================================================================================
    // DATA LOCAL
    //==============================================================================================

    @Provides
    @JvmStatic
    fun provideBlockchainLocalRepository(
        blockchainDao: BlockchainDao
    ): BlockchainLocalRepository = BlockchainLocalRepositoryImpl(blockchainDao)

    //==============================================================================================
    // DATA REMOTE
    //==============================================================================================

    @Provides
    @JvmStatic
    fun provideBlockchainCurrentValueMapper(): BaseMapper<BlockchainCurrentValueRaw, Blockchain> =
        BlockchainCurrentValueMapper()

    @Provides
    @JvmStatic
    fun provideBlockchainMapper(): BaseMapper<BlockchainResponseRaw, List<Blockchain>> =
        BlockchainMapper()

    @Provides
    @JvmStatic
    fun provideBlockchainRemoteRepository(
        blockchainCurrentValueMapper: BaseMapper<BlockchainCurrentValueRaw, Blockchain>,
        blockchainMapper: BaseMapper<BlockchainResponseRaw, List<Blockchain>>,
        blockchainService: BlockchainService,
        remoteExecutor: RemoteExecutor
    ): BlockchainRemoteRepository = BlockchainRemoteRepositoryImpl(
        blockchainCurrentValueMapper,
        blockchainMapper,
        blockchainService,
        remoteExecutor
    )

    @Provides
    @Singleton
    @JvmStatic
    fun provideBlockchainService(retrofit: Retrofit): BlockchainService =
        retrofit.create(BlockchainService::class.java)

    //==============================================================================================
    // DOMAIN
    //==============================================================================================

    @Provides
    @JvmStatic
    fun provideBlockchainRepository(
        localRepository: BlockchainLocalRepository,
        remoteRepository: BlockchainRemoteRepository
    ): BlockchainRepository = BlockchainRepositoryImpl(localRepository, remoteRepository)

    @Provides
    @JvmStatic
    fun provideBlockchainInteractor(
        blockchainRepository: BlockchainRepository
    ): BlockchainInteractor = BlockchainInteractorImpl(blockchainRepository)

    //==============================================================================================
    // PRESENTATION
    //==============================================================================================

    @Provides
    @IntoMap
    @ViewModelKey(BlockchainViewModel::class)
    @JvmStatic
    fun provideBlockchainViewModel(
        blockchainInteractor: BlockchainInteractor,
        viewModelHelper: ViewModelHelper,
        @Named(Constants.COMPUTATION_SCHEDULER) scheduler: Scheduler
    ): ViewModel = BlockchainViewModel(blockchainInteractor, scheduler, viewModelHelper)

}