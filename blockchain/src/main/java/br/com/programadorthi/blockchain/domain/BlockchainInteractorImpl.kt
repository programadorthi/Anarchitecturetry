package br.com.programadorthi.blockchain.domain

import br.com.programadorthi.base.model.Resource
import io.reactivex.Completable
import io.reactivex.Flowable

class BlockchainInteractorImpl(
    private val repository: BlockchainRepository
) : BlockchainInteractor {

    override fun getCurrentMarketPrice(): Flowable<Resource<Blockchain>> =
        repository.getCurrentMarketPrice()

    override fun getAllMarketPrices(): Flowable<Resource<List<Blockchain>>> =
        repository.getAllMarketPrices()

    override fun fetchCurrentMarketPrice(): Completable = repository.fetchCurrentMarketPrice()

    override fun fetchAllMarketPrices(): Completable = repository.fetchAllMarketPrices()

}