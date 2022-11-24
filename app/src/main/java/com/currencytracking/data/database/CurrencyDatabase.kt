package com.currencytracking.data.database

import android.content.Context
import androidx.room.*
import com.currencytracking.data.database.dao.CurrencyDao

@Database(entities = [CurrencyEntity::class], exportSchema = false, version = 1)
abstract class CurrencyDatabase : RoomDatabase() {
    abstract fun favoriteDao(): CurrencyDao

    companion object {
        private var instance: CurrencyDatabase? = null

        fun getInstance(context: Context): CurrencyDatabase {
            if (instance == null) {
                instance =
                    Room.databaseBuilder(context.applicationContext, CurrencyDatabase::class.java,
                        DATA_BASE_NAME)
                        .allowMainThreadQueries()
                        .build()
            }
            return instance!!
        }
    }


}