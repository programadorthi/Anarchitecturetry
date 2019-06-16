package br.com.programadorthi.anarchtecturetry.feature.blockchain.domain

interface BlockchainInteractor {

    suspend fun getCurrentMarketPrice(): Blockchain

    suspend fun getAllMarketPrices(): List<Blockchain>

}