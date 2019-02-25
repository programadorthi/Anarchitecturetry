package br.com.programadorthi.blockchain.data.local

import br.com.programadorthi.blockchain.domain.Blockchain
import io.reactivex.Flowable

interface BlockchainLocalRepository {

    fun getCurrentMarketPrice(): Flowable<Blockchain>

    fun getAllMarketPrices(): Flowable<List<Blockchain>>

    fun insertCurrentValueInTransaction(blockchain: Blockchain)

    fun updateMarketPricesInTransaction(prices: List<Blockchain>)

}