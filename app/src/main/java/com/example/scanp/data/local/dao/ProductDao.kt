package com.example.scanp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.scanp.data.local.entities.ProductEntity

@Dao
interface ProductDao {
    @Query("SELECT * FROM products_table WHERE barcode = :barcode LIMIT 1")
    suspend fun getProduct(barcode: String): ProductEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(product: ProductEntity)
}
