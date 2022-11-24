package com.currencytracking.data.repository

import com.currencytracking.data.database.CurrencyEntity
import com.currencytracking.data.database.CurrencyValue
import com.currencytracking.data.datasource.DataBaseSource
import com.currencytracking.data.datasource.NetworkDataSource
import com.currencytracking.data.model.CurrencySymbols
import com.currencytracking.data.model.CurrencyValues
import com.currencytracking.domain.model.ICurrencyItem
import com.currencytracking.domain.repository.CurrencyRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CurrencyRepositoryImpl(private val networkData: NetworkDataSource,
                             private val dbData: DataBaseSource) : CurrencyRepository {

    init {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                updateCurrenciesList()
            } catch (e: Exception) {
            }
        }
    }

    override suspend fun updateCurrenciesList() {
        val symbols = networkData.getCurrencySymbols().toCurrencyEntity()
        dbData.updateSymbols(symbols)
    }

    override suspend fun updateCurrenciesValues(baseCurrency: String) =
        withContext(Dispatchers.IO) {
            val currencies = networkData.getCurrencyLatest(baseCurrency)
            dbData.updateValues(currencies.toCurrencyValueList())
        }

    override fun getCurrencies(): Flow<List<ICurrencyItem>> {
        return dbData.getAll()
    }

    override suspend fun setFavorite(name: String) {
        dbData.setFavourite(name)
    }

    override suspend fun resetFavorite(name: String) {
        dbData.resetFavourite(name)
    }

    private fun CurrencySymbols.toCurrencyEntity(): List<CurrencyEntity> {
        return symbols.map { CurrencyEntity(name = it.key) }
    }

    private fun CurrencyValues.toCurrencyValueList(): List<CurrencyValue> {
        return rates.map { CurrencyValue(name = it.key, value = it.value.toFloat()) }
    }

}