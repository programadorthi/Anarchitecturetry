package br.com.programadorthi.anarchtecturetry.feature.blockchain.data.remote

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface BlockchainService {

    @GET("stats")
    fun getCurrentMarketPrice(): Single<BlockchainCurrentValueRaw>

    @GET("charts/market-price?format=json")
    fun getMarketPrices(
        @Query("timespan") timespan: String = "1months"
    ): Single<BlockchainResponseRaw>

}