package br.com.programadorthi.anarchtecturetry.feature.blockchain.di

import br.com.programadorthi.anarchtecturetry.database.MainDatabase
import br.com.programadorthi.anarchtecturetry.feature.blockchain.data.BlockchainRepositoryImpl
import br.com.programadorthi.anarchtecturetry.feature.blockchain.data.local.BlockchainDao
import br.com.programadorthi.anarchtecturetry.feature.blockchain.data.local.BlockchainLocalRepository
import br.com.programadorthi.anarchtecturetry.feature.blockchain.data.local.BlockchainLocalRepositoryImpl
import br.com.programadorthi.anarchtecturetry.feature.blockchain.data.remote.*
import br.com.programadorthi.anarchtecturetry.feature.blockchain.domain.Blockchain
import br.com.programadorthi.anarchtecturetry.feature.blockchain.domain.BlockchainInteractor
import br.com.programadorthi.anarchtecturetry.feature.blockchain.domain.BlockchainInteractorImpl
import br.com.programadorthi.anarchtecturetry.feature.blockchain.domain.BlockchainRepository
import br.com.programadorthi.anarchtecturetry.feature.blockchain.presentation.BlockchainViewModel
import br.com.programadorthi.base.exception.CrashConsumer
import br.com.programadorthi.base.formatter.TextFormatter
import br.com.programadorthi.base.remote.BaseRemoteMapper
import br.com.programadorthi.base.remote.RemoteExecutor
import br.com.programadorthi.base.utils.DATE_FORMATTER
import br.com.programadorthi.base.utils.IO_DISPATCHER
import br.com.programadorthi.base.utils.MONEY_FORMATTER
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import retrofit2.Retrofit
import java.math.BigDecimal
import java.util.*
import javax.inject.Named

@Module
object BlockchainModule {

    // =================================================
    // =============== DATA LOCAL ======================
    // =================================================

    @Provides
    @JvmStatic
    fun providesBlockchainDao(database: MainDatabase): BlockchainDao = database.blockchainDao()

    @Provides
    @JvmStatic
    fun provideBlockchainLocalRepository(
        blockchainDao: BlockchainDao,
        crashConsumer: CrashConsumer,
        @Named(IO_DISPATCHER) dispatcher: CoroutineDispatcher
    ): BlockchainLocalRepository = BlockchainLocalRepositoryImpl(blockchainDao, crashConsumer, dispatcher)

    // =================================================
    // =============== DATA REMOTE =====================
    // =================================================

    @Provides
    @JvmStatic
    fun provideBlockchainCurrentValueRemoteMapper(): BaseRemoteMapper<BlockchainCurrentValueRaw, Blockchain> =
        BlockchainCurrentValueRemoteMapper()

    @Provides
    @JvmStatic
    fun provideBlockchainRemoteMapper(): BaseRemoteMapper<BlockchainResponseRaw, List<Blockchain>> =
        BlockchainRemoteMapper()

    @Provides
    @JvmStatic
    fun provideBlockchainService(retrofit: Retrofit): BlockchainService =
        retrofit.create(BlockchainService::class.java)

    @Provides
    @JvmStatic
    fun provideBlockchainRemoteRepository(
        blockchainCurrentValueMapper: BaseRemoteMapper<BlockchainCurrentValueRaw, Blockchain>,
        blockchainMapper: BaseRemoteMapper<BlockchainResponseRaw, List<Blockchain>>,
        blockchainService: BlockchainService,
        remoteExecutor: RemoteExecutor
    ): BlockchainRemoteRepository = BlockchainRemoteRepositoryImpl(
        blockchainCurrentValueMapper = blockchainCurrentValueMapper,
        blockchainMapper = blockchainMapper,
        blockchainService = blockchainService,
        remoteExecutor = remoteExecutor
    )

    // =================================================
    // ================== DOMAIN =======================
    // =================================================

    @Provides
    @JvmStatic
    fun provideBlockchainRepository(
        blockchainLocalRepository: BlockchainLocalRepository,
        blockchainRemoteRepository: BlockchainRemoteRepository
    ): BlockchainRepository = BlockchainRepositoryImpl(blockchainLocalRepository, blockchainRemoteRepository)

    @Provides
    @JvmStatic
    fun provideBlockchainInteractor(
        blockchainRepository: BlockchainRepository
    ): BlockchainInteractor = BlockchainInteractorImpl(blockchainRepository)

    // =================================================
    // =============== PRESENTATION ====================
    // =================================================

    @Provides
    @JvmStatic
    fun provideBlockchainViewModel(
        blockchainInteractor: BlockchainInteractor,
        coroutineScope: CoroutineScope,
        @Named(DATE_FORMATTER) dateFormatter: TextFormatter<Date>,
        @Named(MONEY_FORMATTER) moneyFormatter: TextFormatter<BigDecimal>
    ): BlockchainViewModel =
        BlockchainViewModel(blockchainInteractor, dateFormatter, moneyFormatter, coroutineScope)

}