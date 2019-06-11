package br.com.programadorthi.anarchtecturetry.feature.blockchain.data.local

import br.com.programadorthi.anarchtecturetry.feature.blockchain.domain.Blockchain
import io.reactivex.Flowable
import io.reactivex.functions.Function

class BlockchainLocalRepositoryImpl(
    private val blockchainDao: BlockchainDao,
    private val blockchainCurrentValueLocalMapper: Function<List<BlockchainCurrentValueEntity>, Blockchain>,
    private val blockchainLocalMapper: Function<List<BlockchainEntity>, List<Blockchain>>
) : BlockchainLocalRepository {

    override fun getCurrentMarketPrice(): Flowable<Blockchain> {
        return blockchainDao
            .getCurrentValue()
            .map(blockchainCurrentValueLocalMapper)
    }

    override fun getAllMarketPrices(): Flowable<List<Blockchain>> {
        return blockchainDao
            .getHistoricalMarketPrices()
            .map(blockchainLocalMapper)
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