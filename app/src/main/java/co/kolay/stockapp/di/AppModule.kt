package co.kolay.stockapp.di

import android.app.Application
import androidx.room.Room
import androidx.room.RoomDatabase
import co.kolay.stockapp.data.local.StockDatabase
import co.kolay.stockapp.data.remote.StockApi
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providesStockApi(): StockApi = Retrofit.Builder()
        .baseUrl(StockApi.BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create())
        .build()
        .create()

    @Provides
    @Singleton
    fun providesStockDatabase(appContext: Application): StockDatabase = Room.databaseBuilder(
        appContext,
        StockDatabase::class.java,
        "stockdb.db"
    ).build()

}