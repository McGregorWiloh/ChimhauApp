package com.mcgregor.chimhauapp.repository

import com.mcgregor.chimhauapp.dao.ProductDao
import com.mcgregor.chimhauapp.models.Product
import kotlinx.coroutines.flow.Flow

interface ProductRepository {

    fun getAllProducts(): Flow<List<Product>>

    suspend fun insertProduct(product: Product)

    suspend fun updateProduct(product: Product)

    suspend fun deleteProduct(product: Product)

    suspend fun getProductById(id: Int): Product?

}