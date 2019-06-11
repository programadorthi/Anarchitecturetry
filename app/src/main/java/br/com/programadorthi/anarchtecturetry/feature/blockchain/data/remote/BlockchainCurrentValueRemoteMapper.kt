package br.com.programadorthi.anarchtecturetry.feature.blockchain.data.remote

import br.com.programadorthi.base.remote.BaseRemoteMapper
import br.com.programadorthi.anarchtecturetry.feature.blockchain.domain.Blockchain
import java.util.*

class BlockchainCurrentValueRemoteMapper :
    BaseRemoteMapper<BlockchainCurrentValueRaw, Blockchain>() {

    override fun checkParams(raw: BlockchainCurrentValueRaw, missingFields: MutableList<String>) {
        if (raw.timestamp == null) {
            missingFields.add("timestamp")
        }

        if (raw.value == null) {
            missingFields.add("market_price_usd")
        }
    }

    override fun copyValues(raw: BlockchainCurrentValueRaw): Blockchain {
        return Blockchain(
            // The timestamp returned in the API is milliseconds already
            date = Date(raw.timestamp!!),
            value = raw.value!!
        )
    }

}