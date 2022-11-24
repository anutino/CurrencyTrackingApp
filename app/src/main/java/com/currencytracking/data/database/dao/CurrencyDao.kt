package com.currencytracking.data.database.dao

import androidx.room.*
import com.currencytracking.data.database.*
import kotlinx.coroutines.flow.Flow

@Dao
interface CurrencyDao {

    @Query("SELECT * FROM $CURRENCY_TABLE")
    fun getAll(): Flow<List<CurrencyEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun updateSymbols(currencies: List<CurrencyEntity>)

    @Update(entity = CurrencyEntity::class)
    suspend fun updateValues(currencyValues: List<CurrencyValue>)

    @Query("UPDATE $CURRENCY_TABLE " +
            "SET $CURRENCY_FAVORITE = 1 " +
            "WHERE $CURRENCY_NAME = :name")
    suspend fun setFavourite(name: String)

    @Query("UPDATE $CURRENCY_TABLE " +
            "SET $CURRENCY_FAVORITE = 0 " +
            "WHERE $CURRENCY_NAME = :name")
    suspend fun resetFavourite(name: String)
}