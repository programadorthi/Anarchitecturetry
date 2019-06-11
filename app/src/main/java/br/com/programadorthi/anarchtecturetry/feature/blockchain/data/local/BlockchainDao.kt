package br.com.programadorthi.anarchtecturetry.feature.blockchain.data.local

import androidx.room.*
import io.reactivex.Flowable

@Dao
abstract class BlockchainDao {

    @Insert
    abstract fun insertCurrentValue(entity: BlockchainCurrentValueEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertMarketPrices(prices: List<BlockchainEntity>)

    @Query("DELETE FROM tb_current_value WHERE timestamp > 0")
    abstract fun deleteCurrentValue()

    @Query("DELETE FROM tb_blockchain WHERE timestamp < :timestamp")
    abstract fun deleteMarketPricesLessThanTimestamp(timestamp: Long)

    @Query("SELECT * FROM tb_current_value")
    abstract fun getCurrentValue(): Flowable<List<BlockchainCurrentValueEntity>>

    @Query("SELECT * FROM tb_blockchain ORDER BY timestamp DESC")
    abstract fun getHistoricalMarketPrices(): Flowable<List<BlockchainEntity>>

    @Transaction
    open fun insertCurrentValueInTransaction(entity: BlockchainCurrentValueEntity) {
        deleteCurrentValue()
        insertCurrentValue(entity)
    }

    @Transaction
    open fun updateMarketPricesInTransaction(prices: List<BlockchainEntity>) {
        // The first item is older
        deleteMarketPricesLessThanTimestamp(prices.first().timestamp)
        insertMarketPrices(prices)
    }

}