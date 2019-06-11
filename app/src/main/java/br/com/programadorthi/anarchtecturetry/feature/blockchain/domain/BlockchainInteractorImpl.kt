package br.com.programadorthi.anarchtecturetry.feature.blockchain.domain

import io.reactivex.Completable
import io.reactivex.Flowable

class BlockchainInteractorImpl(
    private val repository: BlockchainRepository
) : BlockchainInteractor {

    override fun getCurrentMarketPrice(): Flowable<Blockchain> =
        repository.getCurrentMarketPrice()

    override fun getAllMarketPrices(): Flowable<List<Blockchain>> =
        repository.getAllMarketPrices()

    override fun fetchCurrentMarketPrice(): Completable = repository.fetchCurrentMarketPrice()

    override fun fetchAllMarketPrices(): Completable = repository.fetchAllMarketPrices()

}