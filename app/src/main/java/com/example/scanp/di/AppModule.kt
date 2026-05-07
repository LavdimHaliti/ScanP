package com.example.scanp.di

import android.app.Application
import androidx.room.Room
import com.example.scanp.data.local.database.ProductDatabase
import com.example.scanp.data.repository.ProductRepository
import com.example.scanp.data.repository.ProductRepositoryImpl
import com.example.scanp.network.OpenFoodFactsApi
import com.example.scanp.util.Constants.BASE_URL
import com.example.scanp.viewmodel.HistoryViewModel
import com.example.scanp.viewmodel.ScanViewModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val networkModule = module {
    single {
        OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }).build()
    }

    single {
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
    single<ProductRepository> {
        ProductRepositoryImpl(get(), get(), get())
    }
}

val viewModelModule = module {
    viewModel { ScanViewModel(get()) }
    viewModel { HistoryViewModel(get()) }
}

