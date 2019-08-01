package br.com.programadorthi.anarchtecturetry.blockchain.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import br.com.programadorthi.base.database.BigDecimalTypeConverter

@Database(
    entities = [
        BlockchainCurrentValueEntity::class,
        BlockchainEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(BigDecimalTypeConverter::class)
abstract class BlockchainDatabase : RoomDatabase() {
    abstract fun blockchainDao(): BlockchainDao

    companion object {

        private const val DATABASE_NAME = "blockchain-db"

        // For Singleton instantiation
        @Volatile private var instance: BlockchainDatabase? = null

        @JvmStatic
        fun getInstance(context: Context): BlockchainDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): BlockchainDatabase {
            return Room.databaseBuilder(
                context, BlockchainDatabase::class.java,
                DATABASE_NAME
            ).build()
        }
    }
}
