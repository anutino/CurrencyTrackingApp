package com.currencytracking.data.repository

import com.currencytracking.data.DataStorageSource
import com.currencytracking.domain.SortType
import com.currencytracking.domain.repository.DataStorageRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow

class DataStorageRepositoryImpl(private val dataSource: DataStorageSource) : DataStorageRepository {
    override fun getBaseCurrency(): Flow<String?> = dataSource.getBaseCurrencyCode()
    override fun setBaseCurrency(name: String) = dataSource.setBaseCurrencyCode(name)
    override fun getSortType(): SharedFlow<SortType> = dataSource.getSortType()
    override fun setSortTypeCode(type: SortType) = dataSource.setSortTypeCode(type)
}