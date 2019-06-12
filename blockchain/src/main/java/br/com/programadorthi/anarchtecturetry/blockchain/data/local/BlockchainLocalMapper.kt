package br.com.programadorthi.anarchtecturetry.blockchain.data.local

import br.com.programadorthi.anarchtecturetry.blockchain.domain.Blockchain
import io.reactivex.functions.Function
import java.util.*

class BlockchainLocalMapper : Function<List<BlockchainEntity>, List<Blockchain>> {

    override fun apply(items: List<BlockchainEntity>): List<Blockchain> {
        if (items.isEmpty()) {
            return emptyList()
        }

        return items.map { entity ->
            Blockchain(
                date = Date(entity.timestamp),
                value = entity.value
            )
        }
    }
}