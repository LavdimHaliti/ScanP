package com.example.scanfood

import android.app.Application
import com.example.scanfood.di.databaseModule
import com.example.scanfood.di.networkModule
import com.example.scanfood.di.repositoryModule
import com.example.scanfood.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class ScanFoodApp : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@ScanFoodApp)
            modules(
                databaseModule,
                networkModule,
                repositoryModule,
                viewModelModule
            )

        }
    }

}