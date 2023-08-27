package co.kolay.stockapp.domain.repository

import co.kolay.stockapp.domain.model.CompanyListing
import co.kolay.stockapp.util.Resource
import kotlinx.coroutines.flow.Flow

interface StockRepository {

    suspend fun getCompanyListings(
        fetchFromRemote: Boolean,
        query: String
    ): Flow<Resource<List<CompanyListing>>>
}