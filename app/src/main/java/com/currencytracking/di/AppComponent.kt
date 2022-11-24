package com.currencytracking.di

import com.currencytracking.domain.repository.CurrencyRepository
import com.currencytracking.domain.repository.DataStorageRepository
import com.currencytracking.domain.usecase.GetCurrencyUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
internal object ViewModelMovieModule {

    @Provides
    @ViewModelScoped
    fun provideUseCase(currencyRepository: CurrencyRepository,
                       dataStorageRepository: DataStorageRepository): GetCurrencyUseCase =
        GetCurrencyUseCase(currencyRepository, dataStorageRepository)
}