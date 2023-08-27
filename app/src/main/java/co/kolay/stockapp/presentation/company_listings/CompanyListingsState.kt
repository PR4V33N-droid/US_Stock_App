package co.kolay.stockapp.presentation.company_listings

import co.kolay.stockapp.domain.model.CompanyListing

data class CompanyListingsState (
    val companies: List<CompanyListing> = emptyList(),
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val searchQuery: String = ""
)
