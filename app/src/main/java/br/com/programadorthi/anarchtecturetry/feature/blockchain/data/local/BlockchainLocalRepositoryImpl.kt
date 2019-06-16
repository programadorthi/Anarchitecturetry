package br.com.programadorthi.anarchtecturetry.feature.blockchain.data.local

import br.com.programadorthi.anarchtecturetry.feature.blockchain.domain.Blockchain
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.util.*

class BlockchainLocalRepositoryImpl(
    private val blockchainDao: BlockchainDao,
    private val dispatcher: CoroutineDispatcher
) : BlockchainLocalRepository {

    override suspend fun getCurrentMarketPrice(): Blockchain = withContext(dispatcher) {
        val currentValues = blockchainDao.getCurrentValue()
        if (currentValues.isEmpty()) {
            return@withContext Blockchain.EMPTY
        }

        val entity = currentValues.first()
        return@withContext Blockchain(
            date = Date(entity.timestamp),
            value = entity.value
        )
    }

    override suspend fun getAllMarketPrices(): List<Blockchain> = withContext(dispatcher) {
        val prices = blockchainDao.getHistoricalMarketPrices()
        if (prices.isEmpty()) {
            return@withContext emptyList<Blockchain>()
        }

        return@withContext prices.map { entity ->
            Blockchain(
                date = Date(entity.timestamp),
                value = entity.value
            )
        }
    }

    override suspend fun insertCurrentValueInTransaction(blockchain: Blockchain) {
        withContext(dispatcher) {
            val entity = BlockchainCurrentValueEntity(
                timestamp = blockchain.date.time,
                value = blockchain.value
            )
            blockchainDao.insertCurrentValueInTransaction(entity)
        }
    }

    override suspend fun updateMarketPricesInTransaction(prices: List<Blockchain>) {
        withContext(dispatcher) {
            val entities = prices.map { blockchain ->
                BlockchainEntity(
                    timestamp = blockchain.date.time,
                    value = blockchain.value
                )
            }
            blockchainDao.updateMarketPricesInTransaction(entities)
        }
    }

}