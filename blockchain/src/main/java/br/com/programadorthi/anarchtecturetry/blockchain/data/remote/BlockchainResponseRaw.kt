package br.com.programadorthi.anarchtecturetry.blockchain.data.remote

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class BlockchainResponseRaw(
    @Json(name = "status") val status: String?,
    @Json(name = "values") val values: List<BlockchainRaw>?
)