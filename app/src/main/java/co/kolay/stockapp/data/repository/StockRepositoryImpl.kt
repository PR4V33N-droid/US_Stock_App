package co.kolay.stockapp.data.repository

import android.util.Log
import co.kolay.stockapp.data.csv.CSVParser
import co.kolay.stockapp.data.local.StockDatabase
import co.kolay.stockapp.data.mapper.toCompanyInfo
import co.kolay.stockapp.data.mapper.toCompanyListing
import co.kolay.stockapp.data.mapper.toCompanyListingEntity
import co.kolay.stockapp.data.remote.StockApi
import co.kolay.stockapp.domain.model.CompanyInfo
import co.kolay.stockapp.domain.model.CompanyListing
import co.kolay.stockapp.domain.model.IntradayInfo
import co.kolay.stockapp.domain.repository.StockRepository
import co.kolay.stockapp.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class StockRepositoryImpl @Inject constructor(
    private val api: StockApi,
    private val db: StockDatabase,
    private val companyListingParser: CSVParser<CompanyListing>,
    private val intradayInfoParser: CSVParser<IntradayInfo>
): StockRepository {

    private val dao = db.dao

    override suspend fun getCompanyListings(
        fetchFromRemote: Boolean,
        query: String,
    ): Flow<Resource<List<CompanyListing>>> {
        return flow {
            emit(Resource.Loading(true))
            val localListings = dao.searchCompanyListing(query)
            emit(Resource.Success(
                data = localListings.map { it.toCompanyListing() }
            ))

            val dbIsEmpty = localListings.isEmpty() && query.isEmpty()
            val shouldJustLoadFromCache = !dbIsEmpty && !fetchFromRemote

            if(shouldJustLoadFromCache){
                emit(Resource.Loading(false))
                return@flow
            }

            val remoteListings = try {
                val response = api.getListings()
                companyListingParser.parse(response.byteStream())
            } catch (e: IOException) {
                e.printStackTrace()
                emit(Resource.Error("couldn't load data"))
                null
            } catch (e: HttpException) {
                e.printStackTrace()
                emit(Resource.Error("couldn't load data"))
                null
            }

            remoteListings?.let { listings ->
                dao.clearCompanyListings()
                dao.insertCompanyListings(
                    listings.map { it.toCompanyListingEntity() }
                )
                emit(Resource.Success(
                    data = dao
                        .searchCompanyListing("")
                        .map { it.toCompanyListing() }
                ))
                emit(Resource.Loading(false))
            }
        }
    }

    override suspend fun getIntradayInfo(symbol: String): Resource<List<IntradayInfo>> {
        return try {
            val response = api.getIntradayInfo(symbol)
            val result = intradayInfoParser.parse(response.byteStream())
            Resource.Success(result)
        } catch (e: IOException){
            e.printStackTrace()
            Resource.Error(
                message = "Couldn't load Intraday Info"
            )
        } catch (e: HttpException){
            e.printStackTrace()
            Resource.Error(
                message = "Couldn't load Intraday Info"
            )
        }
    }

    override suspend fun getCompanyInfo(symbol: String): Resource<CompanyInfo> {
        return try {
            val result = api.getCompanyInfo(symbol)
            Resource.Success(result.toCompanyInfo())
        } catch (e: IOException){
            e.printStackTrace()
            Resource.Error(
                message = "Couldn't load Company Info"
            )
        } catch (e: HttpException){
            e.printStackTrace()
            Resource.Error(
                message = "Couldn't load Company Info"
            )
        }
    }
}