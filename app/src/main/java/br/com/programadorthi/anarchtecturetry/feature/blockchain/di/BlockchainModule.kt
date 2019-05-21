package br.com.programadorthi.anarchtecturetry.feature.blockchain.di

import br.com.programadorthi.anarchtecturetry.database.MainDatabase
import br.com.programadorthi.anarchtecturetry.feature.blockchain.data.BlockchainRepositoryImpl
import br.com.programadorthi.anarchtecturetry.feature.blockchain.data.local.*
import br.com.programadorthi.anarchtecturetry.feature.blockchain.data.remote.*
import br.com.programadorthi.anarchtecturetry.feature.blockchain.domain.Blockchain
import br.com.programadorthi.anarchtecturetry.feature.blockchain.domain.BlockchainInteractor
import br.com.programadorthi.anarchtecturetry.feature.blockchain.domain.BlockchainInteractorImpl
import br.com.programadorthi.anarchtecturetry.feature.blockchain.domain.BlockchainRepository
import br.com.programadorthi.anarchtecturetry.feature.blockchain.presentation.BlockchainViewModel
import br.com.programadorthi.base.presentation.TextFormatter
import br.com.programadorthi.base.remote.BaseRemoteMapper
import br.com.programadorthi.base.remote.RemoteExecutor
import br.com.programadorthi.base.utils.ANDROID_SCHEDULER
import br.com.programadorthi.base.utils.DATE_FORMATTER
import br.com.programadorthi.base.utils.MONEY_FORMATTER
import dagger.Module
import dagger.Provides
import io.reactivex.Scheduler
import io.reactivex.functions.Function
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
    fun provideBlockchainCurrentValueLocalMapper(): Function<List<BlockchainCurrentValueEntity>, Blockchain> =
        BlockchainCurrentValueLocalMapper()

    @Provides
    @JvmStatic
    fun provideBlockchainLocalMapper(): Function<List<BlockchainEntity>, List<Blockchain>> =
        BlockchainLocalMapper()

    @Provides
    @JvmStatic
    fun provideBlockchainLocalRepository(
        blockchainDao: BlockchainDao,
        blockchainCurrentValueLocalMapper: Function<List<BlockchainCurrentValueEntity>, Blockchain>,
        blockchainLocalMapper: Function<List<BlockchainEntity>, List<Blockchain>>
    ): BlockchainLocalRepository = BlockchainLocalRepositoryImpl(
        blockchainDao,
        blockchainCurrentValueLocalMapper,
        blockchainLocalMapper
    )

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
    ): BlockchainRepository =
        BlockchainRepositoryImpl(blockchainLocalRepository, blockchainRemoteRepository)

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
        @Named(DATE_FORMATTER) dateFormatter: TextFormatter<Date>,
        @Named(MONEY_FORMATTER) moneyFormatter: TextFormatter<BigDecimal>,
        @Named(ANDROID_SCHEDULER) scheduler: Scheduler
    ): BlockchainViewModel =
        BlockchainViewModel(blockchainInteractor, dateFormatter, moneyFormatter, scheduler)

}