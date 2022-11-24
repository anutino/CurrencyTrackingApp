package com.currencytracking.data.datasource

import com.currencytracking.data.model.CurrencySymbols
import com.currencytracking.data.model.CurrencyValues
import com.currencytracking.data.network.CurrencyApi

class NetworkDataSource(private val currencyApi: CurrencyApi) {

    suspend fun getCurrencySymbols(): CurrencySymbols {
          return currencyApi.getSymbols()
    }

    suspend fun getCurrencyLatest(baseCurrency: String): CurrencyValues {
       return currencyApi.getLatest(baseCurrency)
    }

}