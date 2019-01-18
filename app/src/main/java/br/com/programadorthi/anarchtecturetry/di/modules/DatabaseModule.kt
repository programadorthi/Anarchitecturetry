package br.com.programadorthi.anarchtecturetry.di.modules

import android.content.Context
import androidx.room.Room
import br.com.programadorthi.anarchtecturetry.MainDatabase
import br.com.programadorthi.blockchain.data.local.BlockchainDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object DatabaseModule {

    @Provides
    @Singleton
    @JvmStatic
    fun provideDatabase(context: Context): MainDatabase = Room
        .databaseBuilder(context, MainDatabase::class.java, "blockchain-database")
        .build()

    @Provides
    @JvmStatic
    fun provideBlockchainDao(database: MainDatabase): BlockchainDao = database.blockchainDao()

}