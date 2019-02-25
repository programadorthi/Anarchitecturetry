package br.com.programadorthi.blockchain.data

import br.com.programadorthi.blockchain.data.local.BlockchainLocalRepository
import br.com.programadorthi.blockchain.data.remote.BlockchainRemoteRepository
import br.com.programadorthi.blockchain.domain.Blockchain
import br.com.programadorthi.blockchain.domain.BlockchainRepository
import io.reactivex.Completable
import io.reactivex.Flowable

class BlockchainRepositoryImpl(
    private val localRepository: BlockchainLocalRepository,
    private val remoteRepository: BlockchainRemoteRepository
) : BlockchainRepository {

    override fun getCurrentMarketPrice(): Flowable<Blockchain> =
        localRepository.getCurrentMarketPrice()

    override fun getAllMarketPrices(): Flowable<List<Blockchain>> =
        localRepository.getAllMarketPrices()

    override fun fetchCurrentMarketPrice(): Completable {
        return remoteRepository
            .getCurrentMarketPrice()
            .doOnSuccess(localRepository::insertCurrentValueInTransaction)
            .ignoreElement()
    }

    override fun fetchAllMarketPrices(): Completable {
        return remoteRepository
            .getAllMarketPrices()
            .doOnSuccess(localRepository::updateMarketPricesInTransaction)
            .ignoreElement()
    }

}