package br.com.programadorthi.anarchtecturetry.blockchain.domain

import br.com.programadorthi.base.shared.LayerResult

interface BlockchainRepository {

    suspend fun getCurrentMarketPrice(): LayerResult<Blockchain>

    suspend fun getAllMarketPrices(): LayerResult<List<Blockchain>>

}