package br.com.programadorthi.anarchtecturetry.blockchain.data.remote

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.math.BigDecimal

@JsonClass(generateAdapter = true)
data class BlockchainCurrentValueRaw(
    @Json(name = TIMESTAMP_FIELD) val timestamp: Long?,
    @Json(name = MARKET_PRICE_FIELD) val value: BigDecimal?
) {
    companion object {
        const val TIMESTAMP_FIELD = "timestamp"
        const val MARKET_PRICE_FIELD = "market_price_usd"
    }
}