package br.com.programadorthi.anarchtecturetry.feature.blockchain.data.local

import androidx.room.*

@Dao
abstract class BlockchainDao {

    @Insert
    abstract suspend fun insertCurrentValue(entity: BlockchainCurrentValueEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertMarketPrices(prices: List<BlockchainEntity>)

    @Query("DELETE FROM tb_current_value WHERE timestamp > 0")
    abstract suspend fun deleteCurrentValue()

    @Query("DELETE FROM tb_blockchain WHERE timestamp < :timestamp")
    abstract suspend fun deleteMarketPricesLessThanTimestamp(timestamp: Long)

    @Query("SELECT * FROM tb_current_value")
    abstract suspend fun getCurrentValue(): List<BlockchainCurrentValueEntity>

    @Query("SELECT * FROM tb_blockchain ORDER BY timestamp DESC")
    abstract suspend fun getHistoricalMarketPrices(): List<BlockchainEntity>

    @Transaction
    open suspend fun insertCurrentValueInTransaction(entity: BlockchainCurrentValueEntity) {
        deleteCurrentValue()
        insertCurrentValue(entity)
    }

    @Transaction
    open suspend fun updateMarketPricesInTransaction(prices: List<BlockchainEntity>) {
        // The first item is older
        deleteMarketPricesLessThanTimestamp(prices.first().timestamp)
        insertMarketPrices(prices)
    }

}