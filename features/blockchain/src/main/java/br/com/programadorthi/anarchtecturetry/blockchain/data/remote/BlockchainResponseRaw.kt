package br.com.programadorthi.anarchtecturetry.blockchain.data.remote

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class BlockchainResponseRaw(
    @Json(name = STATUS_FIELD) val status: String?,
    @Json(name = VALUES_FIELD) val values: List<BlockchainRaw>?
) {
    companion object {
        const val STATUS_FIELD = "status"
        const val VALUES_FIELD = "values"
    }
}