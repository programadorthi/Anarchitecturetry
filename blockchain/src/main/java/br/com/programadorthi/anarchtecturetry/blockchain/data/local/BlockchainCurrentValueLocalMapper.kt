package br.com.programadorthi.anarchtecturetry.blockchain.data.local

import br.com.programadorthi.anarchtecturetry.blockchain.domain.Blockchain
import io.reactivex.functions.Function
import java.util.*

class BlockchainCurrentValueLocalMapper : Function<List<BlockchainCurrentValueEntity>, Blockchain> {

    override fun apply(items: List<BlockchainCurrentValueEntity>): Blockchain {
        if (items.isEmpty()) {
            return Blockchain.EMPTY
        }
        val entity = items.first()
        return Blockchain(
            date = Date(entity.timestamp),
            value = entity.value
        )
    }

}