package br.com.programadorthi.blockchain.di

import br.com.programadorthi.base.network.BaseMapper
import br.com.programadorthi.base.network.RemoteExecutor
import br.com.programadorthi.base.presentation.TextFormatter
import br.com.programadorthi.base.utils.ANDROID_SCHEDULER
import br.com.programadorthi.base.utils.DATE_FORMATTER
import br.com.programadorthi.base.utils.MONEY_FORMATTER
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
import io.reactivex.Scheduler
import retrofit2.Retrofit
import java.math.BigDecimal
import java.util.*
import javax.inject.Named

@Module
object BlockchainModule {

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
    fun provideBlockchainService(retrofit: Retrofit): BlockchainService =
        retrofit.create(BlockchainService::class.java)

    @Provides
    @JvmStatic
    fun provideBlockchainLocalRepository(
        blockchainDao: BlockchainDao
    ): BlockchainLocalRepository = BlockchainLocalRepositoryImpl(blockchainDao)

    @Provides
    @JvmStatic
    fun provideBlockchainRemoteRepository(
        blockchainCurrentValueMapper: BaseMapper<BlockchainCurrentValueRaw, Blockchain>,
        blockchainMapper: BaseMapper<BlockchainResponseRaw, List<Blockchain>>,
        blockchainService: BlockchainService,
        remoteExecutor: RemoteExecutor
    ): BlockchainRemoteRepository = BlockchainRemoteRepositoryImpl(
        blockchainCurrentValueMapper = blockchainCurrentValueMapper,
        blockchainMapper = blockchainMapper,
        blockchainService = blockchainService,
        remoteExecutor = remoteExecutor
    )

    @Provides
    @JvmStatic
    fun provideBlockchainRepository(
        blockchainLocalRepository: BlockchainLocalRepository,
        blockchainRemoteRepository: BlockchainRemoteRepository
    ): BlockchainRepository =
        BlockchainRepositoryImpl(blockchainLocalRepository, blockchainRemoteRepository)

    @Provides
    @JvmStatic
    fun provideBlockchainInteractor(
        blockchainRepository: BlockchainRepository
    ): BlockchainInteractor = BlockchainInteractorImpl(blockchainRepository)

    @Provides
    @JvmStatic
    fun provideBlockchainViewModel(
        blockchainInteractor: BlockchainInteractor,
        @Named(DATE_FORMATTER) dateFormatter: TextFormatter<Date>,
        @Named(MONEY_FORMATTER) moneyFormatter: TextFormatter<BigDecimal>,
        @Named(ANDROID_SCHEDULER) scheduler: Scheduler
    ): BlockchainViewModel =
        BlockchainViewModel(blockchainInteractor, dateFormatter, moneyFormatter, scheduler)

}