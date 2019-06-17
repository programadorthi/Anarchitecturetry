package br.com.programadorthi.anarchtecturetry.feature.blockchain.data.remote

import br.com.programadorthi.anarchtecturetry.feature.blockchain.domain.Blockchain
import br.com.programadorthi.base.shared.LayerResult

interface BlockchainRemoteRepository {

    suspend fun getCurrentMarketPrice(): LayerResult<Blockchain>

    suspend fun getAllMarketPrices(): LayerResult<List<Blockchain>>

}