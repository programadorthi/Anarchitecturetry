package br.com.programadorthi.blockchain.domain

import br.com.programadorthi.base.model.Resource
import io.reactivex.Completable
import io.reactivex.Flowable

interface BlockchainInteractor {

    fun getCurrentMarketPrice(): Flowable<Resource<Blockchain>>

    fun getAllMarketPrices(): Flowable<Resource<List<Blockchain>>>

    fun fetchCurrentMarketPrice(): Completable

    fun fetchAllMarketPrices(): Completable

}