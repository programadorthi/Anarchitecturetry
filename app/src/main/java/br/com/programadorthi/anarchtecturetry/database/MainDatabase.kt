package br.com.programadorthi.anarchtecturetry.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import br.com.programadorthi.blockchain.data.local.BlockchainCurrentValueEntity
import br.com.programadorthi.blockchain.data.local.BlockchainDao
import br.com.programadorthi.blockchain.data.local.BlockchainEntity

@Database(
    entities = [
        BlockchainCurrentValueEntity::class,
        BlockchainEntity::class
    ],
    version = 1
)
@TypeConverters(BigDecimalTypeConverter::class)
abstract class MainDatabase : RoomDatabase() {
    abstract fun blockchainDao(): BlockchainDao
}