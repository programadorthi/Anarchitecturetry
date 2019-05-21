package br.com.programadorthi.anarchtecturetry.feature.blockchain.domain

import br.com.programadorthi.anarchtecturetry.feature.blockchain.domain.Blockchain
import io.reactivex.Completable
import io.reactivex.Flowable

interface BlockchainInteractor {

    fun getCurrentMarketPrice(): Flowable<Blockchain>

    fun getAllMarketPrices(): Flowable<List<Blockchain>>

    fun fetchCurrentMarketPrice(): Completable

    fun fetchAllMarketPrices(): Completable

}