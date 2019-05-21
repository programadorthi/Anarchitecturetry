package br.com.programadorthi.anarchtecturetry.feature.blockchain.data.remote

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.math.BigDecimal

@JsonClass(generateAdapter = true)
data class BlockchainCurrentValueRaw(
    @Json(name = "timestamp") val timestamp: Long?,
    @Json(name = "market_price_usd") val value: BigDecimal?
)