package com.currencytracking.domain.repository

import com.currencytracking.domain.model.ICurrencyItem
import kotlinx.coroutines.flow.Flow

interface CurrencyRepository {

    fun getCurrencies(): Flow<List<ICurrencyItem>>
    suspend fun updateCurrenciesValues(baseCurrency: String)
    suspend fun updateCurrenciesList()
    suspend fun setFavorite(name: String)
    suspend fun resetFavorite(name: String)
}