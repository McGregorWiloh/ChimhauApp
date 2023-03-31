package com.mcgregor.chimhauapp.dao

import androidx.room.*
import com.mcgregor.chimhauapp.models.Product
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {
    @Query("SELECT * FROM product")
    fun getProductList(): Flow<List<Product>>
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertProduct(product: Product)
    @Update
    suspend fun updateProduct(product: Product)
    @Delete
    suspend fun deleteProduct(product: Product)
    @Query("SELECT * FROM product WHERE id = :id")
    suspend fun getProductById(id: Int): Product?
}