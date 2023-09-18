package co.kolay.stockapp.data.repository

import android.util.Log
import co.kolay.stockapp.data.csv.CSVParser
import co.kolay.stockapp.data.local.StockDatabase
import co.kolay.stockapp.data.mapper.toCompanyListing
import co.kolay.stockapp.data.mapper.toCompanyListingEntity
import co.kolay.stockapp.data.remote.StockApi
import co.kolay.stockapp.domain.model.CompanyListing
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
    private val companyListingParser: CSVParser<CompanyListing>
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
                Log.d("FATAL", "response: ${response.byteStream()}")
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

            Log.d("FATAL", "getCompanyListings: $remoteListings")
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
}