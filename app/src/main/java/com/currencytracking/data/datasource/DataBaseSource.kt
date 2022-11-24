package com.currencytracking.data.datasource

import com.currencytracking.data.database.dao.CurrencyDao
import com.currencytracking.data.database.CurrencyEntity
import com.currencytracking.data.database.CurrencyValue
import kotlinx.coroutines.flow.Flow

class DataBaseSource(private val currencyDao: CurrencyDao) {

    fun getAll(): Flow<List<CurrencyEntity>> {
        return currencyDao.getAll()
    }

    suspend fun updateValues(list: List<CurrencyValue>) {
        currencyDao.updateValues(list)
    }

    suspend fun setFavourite(name: String) {
        currencyDao.setFavourite(name)
    }

    suspend fun resetFavourite(name: String) {
        currencyDao.resetFavourite(name)
    }

    fun updateSymbols(symbols: List<CurrencyEntity>) {
        currencyDao.updateSymbols(symbols)
    }

}