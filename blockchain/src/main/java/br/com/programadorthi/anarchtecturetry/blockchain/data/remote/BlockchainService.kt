package br.com.programadorthi.anarchtecturetry.blockchain.data.remote

import br.com.programadorthi.anarchtecturetry.network.ApplicationInterceptor
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface BlockchainService {

    @GET("stats")
    @Headers(ApplicationInterceptor.NO_AUTHORIZATION_HEADER)
    fun getCurrentMarketPrice(): Single<BlockchainCurrentValueRaw>

    @GET("charts/market-price?format=json")
    fun getMarketPrices(
        @Query("timespan") timespan: String = "1months"
    ): Single<BlockchainResponseRaw>

}