package br.com.programadorthi.anarchtecturetry.feature.blockchain.data.local

import br.com.programadorthi.anarchtecturetry.feature.blockchain.domain.Blockchain
import br.com.programadorthi.base.shared.LayerResult

interface BlockchainLocalRepository {

    suspend fun getCurrentMarketPrice(): LayerResult<Blockchain>

    suspend fun getAllMarketPrices(): LayerResult<List<Blockchain>>

    suspend fun insertCurrentValueInTransaction(blockchain: Blockchain): LayerResult<Boolean>

    suspend fun updateMarketPricesInTransaction(prices: List<Blockchain>): LayerResult<Boolean>

}