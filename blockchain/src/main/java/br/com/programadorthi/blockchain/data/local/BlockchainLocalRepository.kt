package br.com.programadorthi.blockchain.data.local

import br.com.programadorthi.base.model.Resource
import br.com.programadorthi.blockchain.domain.Blockchain
import io.reactivex.Flowable

interface BlockchainLocalRepository {

    fun getCurrentMarketPrice(): Flowable<Resource<Blockchain>>

    fun getAllMarketPrices(): Flowable<Resource<List<Blockchain>>>

    fun insertCurrentValueInTransaction(blockchain: Blockchain)

    fun updateMarketPricesInTransaction(prices: List<Blockchain>)

}