package com.currencytracking.domain.repository

import com.currencytracking.domain.SortType
import kotlinx.coroutines.flow.Flow

interface DataStorageRepository {
    fun getBaseCurrency(): Flow<String?>
    fun setBaseCurrency(name: String)
    fun getSortType(): Flow<SortType>
    fun setSortTypeCode(type: SortType)
}