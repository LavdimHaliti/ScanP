package com.example.scanfood.di

import android.app.Application
import androidx.room.Room
import com.example.scanfood.data.local.database.ProductDatabase
import com.example.scanfood.data.repository.ProductRepository
import com.example.scanfood.data.repository.ProductRepositoryImpl
import com.example.scanfood.network.OpenFoodFactsApi
import com.example.scanfood.util.Constants.BASE_URL
import com.example.scanfood.viewmodel.HistoryViewModel
import com.example.scanfood.viewmodel.ProductDetailViewModel
import com.example.scanfood.viewmodel.ScanViewModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.module.dsl.viewModel
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
            .baseUrl(BASE_URL)
            .client(get())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(OpenFoodFactsApi::class.java)
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
        ProductRepositoryImpl(get(), get(), get())
    }
}

val viewModelModule = module {
    viewModel { ScanViewModel(get()) }
    viewModel { HistoryViewModel(get()) }
    viewModel { ProductDetailViewModel(get()) }
}

