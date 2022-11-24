package com.currencytracking.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.currencytracking.domain.SortType
import com.currencytracking.domain.model.ICurrencyItem
import com.currencytracking.domain.usecase.GetCurrencyUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class CurrencyTrackingViewModel @Inject constructor(private var useCase: GetCurrencyUseCase) :
    ViewModel() {

    private var followBaseCurrency: Job? = null
    private val exceptionHandler = CoroutineExceptionHandler { _, exception ->
        Log.e("TAG", "Caught $exception", exception)
    }

    init {
        followBaseCurrency = viewModelScope.launch(exceptionHandler) {
            baseCurrency
                .filter { it != DEFAULT_BASE_VALUE }
                .first()
                .also {
                    withContext(Dispatchers.IO) { useCase.updateCurrenciesValues(it) }
                    followBaseCurrency?.cancelAndJoin()
                }
        }
    }

    val baseCurrency = useCase.getBaseCurrency()
        .filterNotNull()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), DEFAULT_BASE_VALUE)

    val sortType = useCase.getSortType()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), SortType.UNKNOWN)

    private val _currencies = useCase.getCurrencyEntityList()
    val currencySymbols = _currencies.map { currencies -> currencies.map { it.name }.sorted() }
    val currencies = _currencies.combine(sortType) { currencies, sort ->
        when (sort) {
            SortType.NAME_ASC -> currencies.sortedBy { it.name }
            SortType.NAME_DES -> currencies.sortedByDescending { it.name }
            SortType.VALUE_ASC -> currencies.sortedBy { it.value }
            SortType.VALUE_DES -> currencies.sortedByDescending { it.value }
            else -> currencies
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    val currenciesFavourite = currencies.map { it.filter { currency -> currency.isFavourite } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    suspend fun updateCurrencyValues() = withContext(Dispatchers.IO) {
        try {
            if (currencies.value.isEmpty()) useCase.updateCurrencyEntityList()
            useCase.updateCurrenciesValues(baseCurrency.value)
        } catch (e: Exception) {
        }
    }

    fun setBaseCurrency(name: String) {
        viewModelScope.launch(exceptionHandler) {
            useCase.setBaseCurrency(name)
        }
    }

    fun setSortType(type: SortType) {
        useCase.setSortTypeCode(type)
    }

    suspend fun switchFavourite(currency: ICurrencyItem) = withContext(Dispatchers.IO) {
        if (currency.isFavourite) useCase.deleteCurrencyFromFavorite(currency.name)
        else useCase.addToFavouriteCurrency(currency.name)
    }

    companion object {
        private val TAG = CurrencyTrackingViewModel::class.java.simpleName
        private const val DEFAULT_BASE_VALUE = "UNKN"
    }
}