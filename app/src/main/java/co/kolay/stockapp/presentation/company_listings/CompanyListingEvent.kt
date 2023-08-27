package co.kolay.stockapp.presentation.company_listings

sealed class CompanyListingEvent{
    object Refresh: CompanyListingEvent()
    data class OnSearchQueryChange(val query: String): CompanyListingEvent()
}

