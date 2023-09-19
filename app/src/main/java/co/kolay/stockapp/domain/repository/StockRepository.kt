package co.kolay.stockapp.domain.repository

import co.kolay.stockapp.domain.model.CompanyInfo
import co.kolay.stockapp.domain.model.CompanyListing
import co.kolay.stockapp.domain.model.IntradayInfo
import co.kolay.stockapp.util.Resource
import kotlinx.coroutines.flow.Flow

interface StockRepository {

    suspend fun getCompanyListings(
        fetchFromRemote: Boolean,
        query: String
    ): Flow<Resource<List<CompanyListing>>>

    suspend fun getIntradayInfo(
        symbol: String
    ): Resource<List<IntradayInfo>>

    suspend fun getCompanyInfo(
        symbol: String
    ): Resource<CompanyInfo>
}