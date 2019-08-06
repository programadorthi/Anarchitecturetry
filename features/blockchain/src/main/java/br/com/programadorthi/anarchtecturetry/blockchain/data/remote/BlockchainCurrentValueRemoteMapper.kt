package br.com.programadorthi.anarchtecturetry.blockchain.data.remote

import br.com.programadorthi.base.remote.BaseRemoteMapper
import br.com.programadorthi.anarchtecturetry.blockchain.domain.Blockchain
import java.util.*

class BlockchainCurrentValueRemoteMapper : BaseRemoteMapper<BlockchainCurrentValueRaw, Blockchain>() {

    override fun checkParams(raw: BlockchainCurrentValueRaw): List<String> {
        val missingFields = mutableListOf<String>()
        if (raw.timestamp == null) {
            missingFields.add(BlockchainCurrentValueRaw.TIMESTAMP_FIELD)
        }

        if (raw.value == null) {
            missingFields.add(BlockchainCurrentValueRaw.MARKET_PRICE_FIELD)
        }
        return missingFields
    }

    override fun copyValues(raw: BlockchainCurrentValueRaw): Blockchain {
        return Blockchain(
            // The timestamp returned in the API is milliseconds already
            date = Date(raw.timestamp!!),
            value = raw.value!!
        )
    }

}