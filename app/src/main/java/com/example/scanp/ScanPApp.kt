package com.example.scanp

import android.app.Application
import com.example.scanp.di.databaseModule
import com.example.scanp.di.networkModule
import com.example.scanp.di.repositoryModule
import com.example.scanp.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class ScanPApp : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@ScanPApp)
            modules(
                databaseModule,
                networkModule,
                repositoryModule,
                viewModelModule
            )

        }
    }

}