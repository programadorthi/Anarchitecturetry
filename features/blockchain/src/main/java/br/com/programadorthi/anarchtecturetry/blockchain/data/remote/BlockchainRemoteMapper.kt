package br.com.programadorthi.anarchtecturetry.blockchain.data.remote

import br.com.programadorthi.anarchtecturetry.blockchain.domain.Blockchain
import br.com.programadorthi.base.remote.BaseRemoteMapper
import java.math.BigDecimal
import java.util.*

class BlockchainRemoteMapper : BaseRemoteMapper<BlockchainResponseRaw, List<Blockchain>>() {

    override fun checkParams(raw: BlockchainResponseRaw): List<String> {
        val missingFields = mutableListOf<String>()
        if (raw.status == null) {
            missingFields.add(BlockchainResponseRaw.STATUS_FIELD)
        }

        if (raw.values == null) {
            missingFields.add(BlockchainResponseRaw.VALUES_FIELD)
        }
        return missingFields
    }

    override fun copyValues(raw: BlockchainResponseRaw): List<Blockchain> {
        val values = raw.values!!
        if (values.isEmpty()) {
            return emptyList()
        }

        return values.map { blockchainRaw ->
            Blockchain(
                date = convertTimestamp(blockchainRaw.timestamp),
                value = blockchainRaw.value ?: BigDecimal.ZERO
            )
        }
    }

    /**
     * The timestamp returned by API is an Epoch Unix Timestamp.
     * We need multiply by 1000 to convert the epoch to milliseconds
     */
    private fun convertTimestamp(timestamp: Long?): Date {
        if (timestamp == null) {
            return Date()
        }
        return Date(timestamp * 1000L)
    }

}