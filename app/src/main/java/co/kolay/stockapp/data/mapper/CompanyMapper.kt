package co.kolay.stockapp.data.mapper

import co.kolay.stockapp.data.local.CompanyListingEntity
import co.kolay.stockapp.data.remote.dto.CompanyInfoDto
import co.kolay.stockapp.domain.model.CompanyInfo
import co.kolay.stockapp.domain.model.CompanyListing

fun CompanyListingEntity.toCompanyListing(): CompanyListing {
    return CompanyListing(
        name = name,
        symbol = symbol,
        exchange = exchange
    )
}

fun CompanyListing.toCompanyListingEntity(): CompanyListingEntity {
    return CompanyListingEntity(
        name = name,
        symbol = symbol,
        exchange = exchange
    )
}

fun CompanyInfoDto.toCompanyInfo(): CompanyInfo{
    return CompanyInfo(
        name = name ?: "",
        symbol = symbol ?: "",
        description = description ?: "",
        country = country ?: "",
        industry = industry ?: ""
    )
}