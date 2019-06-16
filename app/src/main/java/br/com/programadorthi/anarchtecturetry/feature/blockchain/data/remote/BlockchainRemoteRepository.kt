package br.com.programadorthi.anarchtecturetry.feature.blockchain.data.remote

import br.com.programadorthi.anarchtecturetry.feature.blockchain.domain.Blockchain

interface BlockchainRemoteRepository {

    suspend fun getCurrentMarketPrice(): Blockchain

    suspend fun getAllMarketPrices(): List<Blockchain>

}