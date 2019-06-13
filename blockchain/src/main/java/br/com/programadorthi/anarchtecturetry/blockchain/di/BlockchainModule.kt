package br.com.programadorthi.anarchtecturetry.blockchain.di

import androidx.room.Room
import br.com.programadorthi.anarchtecturetry.blockchain.data.BlockchainRepositoryImpl
import br.com.programadorthi.anarchtecturetry.blockchain.data.local.*
import br.com.programadorthi.anarchtecturetry.blockchain.data.remote.*
import br.com.programadorthi.anarchtecturetry.blockchain.domain.Blockchain
import br.com.programadorthi.anarchtecturetry.blockchain.domain.BlockchainInteractor
import br.com.programadorthi.anarchtecturetry.blockchain.domain.BlockchainInteractorImpl
import br.com.programadorthi.anarchtecturetry.blockchain.domain.BlockchainRepository
import br.com.programadorthi.anarchtecturetry.blockchain.presentation.BlockchainViewModel
import br.com.programadorthi.base.remote.BaseRemoteMapper
import br.com.programadorthi.base.utils.ANDROID_SCHEDULER
import br.com.programadorthi.base.utils.DATE_FORMATTER
import br.com.programadorthi.base.utils.MONEY_FORMATTER
import io.reactivex.functions.Function
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit

private const val DATABASE_NAME = "blockchain-database"

private const val LOCAL_MAPPER_CURRENT_VALUE = "LOCAL_MAPPER_CURRENT_VALUE"
private const val LOCAL_MAPPER_LIST_VALUE = "LOCAL_MAPPER_LIST_VALUE"
private const val REMOTE_MAPPER_CURRENT_VALUE = "REMOTE_MAPPER_CURRENT_VALUE"
private const val REMOTE_MAPPER_LIST_VALUE = "REMOTE_MAPPER_LIST_VALUE"

val blockchainModule = module {

    // =================================================
    // =============== DATA LOCAL ======================
    // =================================================

    single { Room.databaseBuilder(androidContext(), BlockchainDatabase::class.java, DATABASE_NAME).build() }

    factory { get<BlockchainDatabase>().blockchainDao() }

    factory<Function<List<BlockchainCurrentValueEntity>, Blockchain>>(named(name = LOCAL_MAPPER_CURRENT_VALUE)) {
        BlockchainCurrentValueLocalMapper()
    }

    factory<Function<List<BlockchainEntity>, List<Blockchain>>>(named(name = LOCAL_MAPPER_LIST_VALUE)) {
        BlockchainLocalMapper()
    }

    factory<BlockchainLocalRepository> {
        BlockchainLocalRepositoryImpl(
            blockchainDao = get(),
            blockchainCurrentValueLocalMapper = get(named(name = LOCAL_MAPPER_CURRENT_VALUE)),
            blockchainLocalMapper = get(named(name = LOCAL_MAPPER_LIST_VALUE))
        )
    }

    // =================================================
    // =============== DATA REMOTE =====================
    // =================================================

    factory<BaseRemoteMapper<BlockchainCurrentValueRaw, Blockchain>>(named(name = REMOTE_MAPPER_CURRENT_VALUE)) {
        BlockchainCurrentValueRemoteMapper()
    }

    factory<BaseRemoteMapper<BlockchainResponseRaw, List<Blockchain>>>(named(name = REMOTE_MAPPER_LIST_VALUE)) {
        BlockchainRemoteMapper()
    }

    factory<BlockchainService> { get<Retrofit>().create(BlockchainService::class.java) }

    factory<BlockchainRemoteRepository> {
        BlockchainRemoteRepositoryImpl(
            blockchainCurrentValueMapper = get(named(name = REMOTE_MAPPER_CURRENT_VALUE)),
            blockchainMapper = get(named(name = REMOTE_MAPPER_LIST_VALUE)),
            blockchainService = get(),
            remoteExecutor = get()
        )
    }

    // =================================================
    // ================== DOMAIN =======================
    // =================================================

    factory<BlockchainRepository> { BlockchainRepositoryImpl(localRepository = get(), remoteRepository = get()) }

    factory<BlockchainInteractor> { BlockchainInteractorImpl(repository = get()) }

    // =================================================
    // =============== PRESENTATION ====================
    // =================================================

    viewModel {
        BlockchainViewModel(
            blockchainInteractor = get(),
            dateFormatter = get(named(name = DATE_FORMATTER)),
            moneyFormatter = get(named(name = MONEY_FORMATTER)),
            scheduler = get(named(name = ANDROID_SCHEDULER))
        )
    }
}
