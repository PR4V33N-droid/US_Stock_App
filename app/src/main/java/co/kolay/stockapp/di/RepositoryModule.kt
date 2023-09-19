package co.kolay.stockapp.di

import co.kolay.stockapp.data.csv.CSVParser
import co.kolay.stockapp.data.csv.CompanyListingParser
import co.kolay.stockapp.data.csv.IntradayInfoParser
import co.kolay.stockapp.data.repository.StockRepositoryImpl
import co.kolay.stockapp.domain.model.CompanyListing
import co.kolay.stockapp.domain.model.IntradayInfo
import co.kolay.stockapp.domain.repository.StockRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindCompanyListingParser(
        companyListingParser: CompanyListingParser
    ): CSVParser<CompanyListing>

    @Binds
    @Singleton
    abstract fun bindIntradayinfoParser(
        intradayInfoParser: IntradayInfoParser
    ): CSVParser<IntradayInfo>

    @Binds
    @Singleton
    abstract fun bindStockRepository(
        stockRepositoryImpl: StockRepositoryImpl
    ): StockRepository

}