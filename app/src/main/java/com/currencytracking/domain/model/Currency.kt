package com.currencytracking.domain.model

interface ICurrencyItem {
    val name: String
    val value: Float
    val isFavourite: Boolean
}