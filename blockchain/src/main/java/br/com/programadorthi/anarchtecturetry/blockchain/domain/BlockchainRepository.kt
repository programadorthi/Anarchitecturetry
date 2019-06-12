package br.com.programadorthi.anarchtecturetry.blockchain.domain

import io.reactivex.Completable
import io.reactivex.Flowable

interface BlockchainRepository {

    fun getCurrentMarketPrice(): Flowable<Blockchain>

    fun getAllMarketPrices(): Flowable<List<Blockchain>>

    fun fetchCurrentMarketPrice(): Completable

    fun fetchAllMarketPrices(): Completable

}