package br.com.programadorthi.anarchtecturetry.di.modules

import android.content.Context
import androidx.room.Room
import br.com.programadorthi.anarchtecturetry.database.MainDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object DatabaseModule {

    @Provides
    @Singleton
    @JvmStatic
    fun provideDatabase(context: Context): MainDatabase {
        return Room.databaseBuilder(context, MainDatabase::class.java, "main-database").build()
    }
}