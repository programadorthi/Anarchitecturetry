package br.com.programadorthi.anarchtecturetry.feature.blockchain.data.remote

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.math.BigDecimal

@JsonClass(generateAdapter = true)
data class BlockchainRaw(
    @Json(name = "x") val timestamp: Long?,
    @Json(name = "y") val value: BigDecimal?
)