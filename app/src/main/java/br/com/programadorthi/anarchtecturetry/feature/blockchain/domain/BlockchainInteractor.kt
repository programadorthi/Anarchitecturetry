package br.com.programadorthi.anarchtecturetry.feature.blockchain.domain

import br.com.programadorthi.base.shared.LayerResult

interface BlockchainInteractor {

    suspend fun getCurrentMarketPrice(): LayerResult<Blockchain>

    suspend fun getAllMarketPrices(): LayerResult<List<Blockchain>>

}