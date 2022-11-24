package com.currencytracking.data.network

import com.currencytracking.data.model.CurrencySymbols
import com.currencytracking.data.model.CurrencyValues
import retrofit2.http.GET
import retrofit2.http.Query

interface CurrencyApi {

    @GET("/exchangerates_data/symbols")
    suspend fun getSymbols(): CurrencySymbols

    @GET("/exchangerates_data/latest")
    suspend fun getLatest(@Query("base") baseCurrency: String): CurrencyValues

}