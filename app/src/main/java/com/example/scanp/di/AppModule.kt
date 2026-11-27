package com.example.scanp.di

import android.app.Application
import androidx.room.Room
import com.example.scanp.data.local.database.ProductDatabase
import com.example.scanp.data.repository.ProductRepository
import com.example.scanp.data.repository.ProductRepositoryImpl
import com.example.scanp.network.OpenFactsSearchApi
import com.example.scanp.network.OpenFoodFactsApi
import com.example.scanp.util.Constants.BEAUTY_BASE_URL
import com.example.scanp.util.Constants.BEAUTY_SEARCH
import com.example.scanp.util.Constants.FOOD_BASE_URL
import com.example.scanp.util.Constants.FOOD_SEARCH
import com.example.scanp.viewmodel.HistoryViewModel
import com.example.scanp.viewmodel.ProductDetailViewModel
import com.example.scanp.viewmodel.ScanViewModel
import com.example.scanp.viewmodel.SearchViewModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.module.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val networkModule = module {
    factory {
        OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }).build()
    }

    factory {
        Retrofit.Builder()
            .baseUrl(FOOD_BASE_URL)
            .client(get())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(OpenFoodFactsApi::class.java)
    }

    single<OpenFactsSearchApi>(named(FOOD_SEARCH)){
        provideSearchApi(get(), FOOD_BASE_URL)
    }
    single<OpenFactsSearchApi>(named(BEAUTY_SEARCH)){
        provideSearchApi(get(), BEAUTY_BASE_URL)
    }
}

val databaseModule = module {
    single<ProductDatabase> {
        Room.databaseBuilder(
            get<Application>(),
            ProductDatabase::class.java,
            "product_database"
        )
            .fallbackToDestructiveMigration(true)
            .build()
    }

    single { get<ProductDatabase>().productDao() }
    single { get<ProductDatabase>().scanHistoryDao() }

}

val repositoryModule = module {
    factory<ProductRepository> {
        ProductRepositoryImpl(
            get(),
            get(named(FOOD_SEARCH)),
            get(named(BEAUTY_SEARCH)),
            get(),
            get()
        )
    }
}

val viewModelModule = module {
    viewModel { ScanViewModel(get()) }
    viewModel { HistoryViewModel(get()) }
    viewModel { ProductDetailViewModel(get()) }
    viewModel { SearchViewModel(get()) }
}

private fun provideSearchApi(okHttpClient: OkHttpClient, baseUrl: String): OpenFactsSearchApi = Retrofit.Builder()
    .baseUrl(baseUrl)
    .client(okHttpClient)
    .addConverterFactory(GsonConverterFactory.create())
    .build()
    .create(OpenFactsSearchApi::class.java)

