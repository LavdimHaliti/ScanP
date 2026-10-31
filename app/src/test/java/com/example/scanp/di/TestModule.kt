package com.example.scanp.di

import com.example.scanp.data.repository.ProductRepository
import io.mockk.mockk
import org.koin.dsl.module

// In tests, use a MOCK repository instead of the real one
val testModule = module {
    // relaxed = true means: "don't complain if a method isn't explicitly stubbed"
    single<ProductRepository> { mockk(relaxed = true) }
}