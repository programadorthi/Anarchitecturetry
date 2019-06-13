package br.com.programadorthi.anarchtecturetry.blockchain.data.remote

import br.com.programadorthi.base.remote.BaseRemoteMapper
import br.com.programadorthi.anarchtecturetry.blockchain.domain.Blockchain
import java.util.*

class BlockchainCurrentValueRemoteMapper : BaseRemoteMapper<BlockchainCurrentValueRaw, Blockchain>() {

    override fun checkRequiredParamsBeforeCopyValues(raw: BlockchainCurrentValueRaw): List<String> {
        val missingFields = mutableListOf<String>()
        if (raw.timestamp == null) {
            missingFields.add("timestamp")
        }

        if (raw.value == null) {
            missingFields.add("market_price_usd")
        }
        return missingFields
    }

    override fun copyValuesAfterCheckRequiredParams(raw: BlockchainCurrentValueRaw): Blockchain {
        return Blockchain(
            // The timestamp returned in the API is milliseconds already
            date = Date(raw.timestamp!!),
            value = raw.value!!
        )
    }

}