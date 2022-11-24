package com.currencytracking.domain.usecase

import com.currencytracking.domain.SortType
import com.currencytracking.domain.model.ICurrencyItem
import com.currencytracking.domain.repository.CurrencyRepository
import com.currencytracking.domain.repository.DataStorageRepository
import kotlinx.coroutines.flow.Flow

class GetCurrencyUseCase(
    private val currencyRepository: CurrencyRepository,
    private val dataStorageRepository: DataStorageRepository) {

    fun getCurrencyEntityList(): Flow<List<ICurrencyItem>> {
        return currencyRepository.getCurrencies()
    }

    suspend fun updateCurrenciesValues(baseCurrency: String){
        currencyRepository.updateCurrenciesValues(baseCurrency)

    }

    fun updateCurrencyEntityList(): Flow<List<ICurrencyItem>> {
        return currencyRepository.getCurrencies()
    }

    suspend fun addToFavouriteCurrency(name: String) {
        currencyRepository.setFavorite(name)
    }

    suspend fun deleteCurrencyFromFavorite(name: String) {
        currencyRepository.resetFavorite(name)
    }

    fun setSortTypeCode(type: SortType){
        dataStorageRepository.setSortTypeCode(type)
    }

    fun getBaseCurrency(): Flow<String?> {
        return dataStorageRepository.getBaseCurrency()
    }

    fun getSortType():Flow<SortType> {
        return dataStorageRepository.getSortType()
    }

    suspend fun setBaseCurrency(name: String) {
        dataStorageRepository.setBaseCurrency(name)
        currencyRepository.updateCurrenciesValues(name)
    }

}