package com.currencytracking.data.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.currencytracking.domain.model.ICurrencyItem

const val DATA_BASE_NAME = "favorite_db"
const val CURRENCY_TABLE = "currency"
const val CURRENCY_NAME = "name"
const val CURRENCY_VALUE = "value"
const val CURRENCY_FAVORITE = "is_favourite"

@Entity(tableName = CURRENCY_TABLE)
data class CurrencyEntity(@PrimaryKey
                          @ColumnInfo(name = CURRENCY_NAME)
                          override val name: String,
                          @ColumnInfo(name = CURRENCY_VALUE)
                          override val value: Float = 0f,
                          @ColumnInfo(name = CURRENCY_FAVORITE)
                          override val isFavourite: Boolean = false)
    : ICurrencyItem

data class CurrencyValue(
    val name: String,
    val value: Float
)

data class CurrencySymbols(
    val name: String
)