package br.com.programadorthi.anarchtecturetry.feature.blockchain.domain

interface BlockchainRepository {

    suspend fun getCurrentMarketPrice(): Blockchain

    suspend fun getAllMarketPrices(): List<Blockchain>

}