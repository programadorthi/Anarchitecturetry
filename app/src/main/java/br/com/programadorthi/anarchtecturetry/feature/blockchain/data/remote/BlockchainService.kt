package br.com.programadorthi.anarchtecturetry.feature.blockchain.data.remote

import retrofit2.http.GET
import retrofit2.http.Query

interface BlockchainService {

    @GET("stats")
    suspend fun getCurrentMarketPrice(): BlockchainCurrentValueRaw

    @GET("charts/market-price?format=json")
    suspend fun getMarketPrices(
        @Query("timespan") timespan: String = "1months"
    ): BlockchainResponseRaw

}