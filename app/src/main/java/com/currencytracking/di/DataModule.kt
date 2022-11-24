package com.currencytracking.di

import android.app.Application
import android.content.Context
import com.currencytracking.data.DataStorageSource
import com.currencytracking.data.database.dao.CurrencyDao
import com.currencytracking.data.database.CurrencyDatabase
import com.currencytracking.data.datasource.DataBaseSource
import com.currencytracking.data.datasource.NetworkDataSource
import com.currencytracking.data.network.CurrencyApi
import com.currencytracking.data.network.RetrofitHelper
import com.currencytracking.data.repository.CurrencyRepositoryImpl
import com.currencytracking.data.repository.DataStorageRepositoryImpl
import com.currencytracking.domain.repository.CurrencyRepository
import com.currencytracking.domain.repository.DataStorageRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DataModule {
    @Provides
    @Singleton
    fun provideDataStorageSource(context: Context) = DataStorageSource(context)

    @Provides
    @Singleton
    fun provideDataStorageRepository(dataStorageSource: DataStorageSource): DataStorageRepository =
        DataStorageRepositoryImpl(dataStorageSource)


    @Provides
    @Singleton
    fun provideDatabase(application: Application): CurrencyDatabase {
        return CurrencyDatabase.getInstance(application)
    }

    @Provides
    @Singleton
    fun provideFavoriteCurrencyDao(db: CurrencyDatabase): CurrencyDao {
        return db.favoriteDao()
    }

    @Provides
    @Singleton
    fun provideRetrofit() =
        RetrofitHelper.getInstance()

    @Provides
    @Singleton
    fun provideCurrencyApi(retrofit: Retrofit): CurrencyApi =
        retrofit.create(CurrencyApi::class.java)

    @Provides
    @Singleton
    fun provideDataBaseSource(
        favoriteCurrencyDao: CurrencyDao
    ): DataBaseSource =
        DataBaseSource(favoriteCurrencyDao)

    @Provides
    @Singleton
    fun provideNetworkDataSource(
        currencyApi: CurrencyApi
    ): NetworkDataSource = NetworkDataSource(currencyApi)

    @Provides
    @Singleton
    fun provideCurrencyRepositoryImp(networkDataSource: NetworkDataSource,
                                     dataBaseSource: DataBaseSource): CurrencyRepository =
        CurrencyRepositoryImpl(networkDataSource, dataBaseSource)
}