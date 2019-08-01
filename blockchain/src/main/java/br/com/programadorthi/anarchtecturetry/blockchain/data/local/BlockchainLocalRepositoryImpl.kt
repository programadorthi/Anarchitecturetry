package br.com.programadorthi.anarchtecturetry.blockchain.data.local

import br.com.programadorthi.anarchtecturetry.blockchain.domain.Blockchain
import br.com.programadorthi.base.exception.CrashConsumer
import br.com.programadorthi.base.shared.LayerResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.util.*

class BlockchainLocalRepositoryImpl(
    private val blockchainDao: BlockchainDao,
    private val crashConsumer: CrashConsumer,
    private val dispatcher: CoroutineDispatcher
) : BlockchainLocalRepository {

    override suspend fun getCurrentMarketPrice(): LayerResult<Blockchain> {
        return withContext(dispatcher) {
            val currentValues = try {
                blockchainDao.getCurrentValue()
            } catch (ex: Exception) {
                crashConsumer.report(ex)
                return@withContext LayerResult.fromException(ex)
            }

            if (currentValues.isEmpty()) {
                return@withContext LayerResult.Success(Blockchain.EMPTY)
            }

            val entity = currentValues.first()
            val blockchain = Blockchain(
                date = Date(entity.timestamp),
                value = entity.value
            )
            return@withContext LayerResult.Success(blockchain)
        }
    }

    override suspend fun getAllMarketPrices(): LayerResult<List<Blockchain>> {
        return withContext(dispatcher) {
            val prices = try {
                blockchainDao.getHistoricalMarketPrices()
            } catch (ex: Exception) {
                crashConsumer.report(ex)
                return@withContext LayerResult.fromException(ex)
            }

            if (prices.isEmpty()) {
                return@withContext LayerResult.Success(emptyList<Blockchain>())
            }

            val mapped = prices.map { entity ->
                Blockchain(
                    date = Date(entity.timestamp),
                    value = entity.value
                )
            }
            return@withContext LayerResult.Success(mapped)
        }
    }

    override suspend fun insertCurrentValueInTransaction(
        blockchain: Blockchain
    ): LayerResult<Boolean> {
        return withContext(dispatcher) {
            if (blockchain == Blockchain.EMPTY) {
                return@withContext LayerResult.success(false)
            }

            return@withContext try {
                val entity = BlockchainCurrentValueEntity(
                    timestamp = blockchain.date.time,
                    value = blockchain.value
                )
                blockchainDao.insertCurrentValueInTransaction(entity)
                LayerResult.success(true)
            } catch (ex: Exception) {
                crashConsumer.report(ex)
                return@withContext LayerResult.fromException(ex)
            }
        }
    }

    override suspend fun updateMarketPricesInTransaction(
        prices: List<Blockchain>
    ): LayerResult<Boolean> {
        return withContext(dispatcher) {
            if (prices.isEmpty()) {
                return@withContext LayerResult.success(false)
            }

            return@withContext try {
                val entities = prices.map { blockchain ->
                    BlockchainEntity(
                        timestamp = blockchain.date.time,
                        value = blockchain.value
                    )
                }
                blockchainDao.updateMarketPricesInTransaction(entities)
                LayerResult.success(true)
            } catch (ex: Exception) {
                crashConsumer.report(ex)
                return@withContext LayerResult.fromException(ex)
            }
        }
    }

}