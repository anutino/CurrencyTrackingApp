package com.currencytracking.data.model

data class CurrencySymbols(val symbols: Map<String, String>)

data class CurrencyValues(
    val base: String,
    val date: String,
    val rates: Map<String, Double>
)
