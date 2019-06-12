package br.com.programadorthi.anarchtecturetry.blockchain.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import br.com.programadorthi.base.database.BigDecimalTypeConverter

@Database(
    entities = [
        BlockchainCurrentValueEntity::class,
        BlockchainEntity::class
    ],
    version = 1
)
@TypeConverters(BigDecimalTypeConverter::class)
abstract class BlockchainDatabase : RoomDatabase() {
    abstract fun blockchainDao(): BlockchainDao
}
