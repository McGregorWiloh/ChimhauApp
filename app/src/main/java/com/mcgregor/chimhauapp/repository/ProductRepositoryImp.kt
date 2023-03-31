package com.mcgregor.chimhauapp.repository

import com.mcgregor.chimhauapp.dao.ProductDao
import com.mcgregor.chimhauapp.models.Product
import kotlinx.coroutines.flow.Flow

class ProductRepositoryImp(
    private val productDao: ProductDao
): ProductRepository {

    override fun getAllProducts(): Flow<List<Product>> {
        return productDao.getProductList()
    }

    override suspend fun insertProduct(product: Product) {
        productDao.insertProduct(product)
    }

    override suspend fun updateProduct(product: Product) {
        productDao.updateProduct(product)
    }

    override suspend fun deleteProduct(product: Product) {
        productDao.deleteProduct(product)
    }

    override suspend fun getProductById(id: Int): Product? {
        return productDao.getProductById(id)
    }

}