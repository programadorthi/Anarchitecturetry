package br.com.programadorthi.blockchain.data.local

import br.com.programadorthi.blockchain.domain.Blockchain
import io.reactivex.Flowable
import java.util.*

class BlockchainLocalRepositoryImpl(
    private val blockchainDao: BlockchainDao
) : BlockchainLocalRepository {

    override fun getCurrentMarketPrice(): Flowable<Blockchain> {
        return blockchainDao
            .getCurrentValue()
            .map { result ->
                if (result.isEmpty()) {
                    return@map Blockchain.EMPTY
                }

                val entity = result.first()
                return@map Blockchain(
                    date = Date(entity.timestamp),
                    value = entity.value
                )
            }
    }

    override fun getAllMarketPrices(): Flowable<List<Blockchain>> {
        return blockchainDao
            .getHistoricalMarketPrices()
            .map { list ->
                list.map { entity ->
                    Blockchain(
                        date = Date(entity.timestamp),
                        value = entity.value
                    )
                }
            }
    }

    override fun insertCurrentValueInTransaction(blockchain: Blockchain) {
        val entity = BlockchainCurrentValueEntity(
            timestamp = blockchain.date.time,
            value = blockchain.value
        )
        blockchainDao.insertCurrentValueInTransaction(entity)
    }

    override fun updateMarketPricesInTransaction(prices: List<Blockchain>) {
        val entities = prices.map { blockchain ->
            BlockchainEntity(
                timestamp = blockchain.date.time,
                value = blockchain.value
            )
        }
        blockchainDao.updateMarketPricesInTransaction(entities)
    }

}