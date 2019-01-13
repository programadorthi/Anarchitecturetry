package br.com.programadorthi.blockchain.data.local

import br.com.programadorthi.base.model.Resource
import br.com.programadorthi.blockchain.domain.Blockchain
import io.reactivex.Flowable
import java.util.*

class BlockchainLocalRepositoryImpl(
    private val blockchainDao: BlockchainDao
) : BlockchainLocalRepository {

    override fun getCurrentMarketPrice(): Flowable<Resource<Blockchain>> {
        return blockchainDao
            .getCurrentValue()
            .map { result ->
                if (result.isEmpty()) {
                    return@map Resource.success(Blockchain.EMPTY)
                }

                val entity = result.first()
                val blockchain = Blockchain(
                    date = Date(entity.timestamp),
                    value = entity.value
                )
                return@map Resource.success(blockchain)
            }
            .onErrorReturn { err -> Resource.error(err, Blockchain.EMPTY) }
    }

    override fun getAllMarketPrices(): Flowable<Resource<List<Blockchain>>> {
        return blockchainDao
            .getHistoricalMarketPrices()
            .map { list ->
                val prices = list.map { entity ->
                    Blockchain(
                        date = Date(entity.timestamp),
                        value = entity.value
                    )
                }
                return@map Resource.success(prices)
            }
            .onErrorReturn { err -> Resource.error(err, emptyList()) }
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