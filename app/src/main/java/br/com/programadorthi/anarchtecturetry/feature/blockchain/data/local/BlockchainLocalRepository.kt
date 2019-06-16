package br.com.programadorthi.anarchtecturetry.feature.blockchain.data.local

import br.com.programadorthi.anarchtecturetry.feature.blockchain.domain.Blockchain

interface BlockchainLocalRepository {

    suspend fun getCurrentMarketPrice(): Blockchain

    suspend fun getAllMarketPrices(): List<Blockchain>

    suspend fun insertCurrentValueInTransaction(blockchain: Blockchain)

    suspend fun updateMarketPricesInTransaction(prices: List<Blockchain>)

}