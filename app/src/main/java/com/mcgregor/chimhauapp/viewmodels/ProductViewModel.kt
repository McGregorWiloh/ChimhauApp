package com.mcgregor.chimhauapp.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mcgregor.chimhauapp.models.Product
import com.mcgregor.chimhauapp.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val productRepository: ProductRepository, savedStateHandle: SavedStateHandle
) : ViewModel() {
    /*var product by mutableStateOf<Product?>(null)
        private set

    var productName by mutableStateOf("")
        private set

    var productPrice by mutableStateOf("")
        private set*/

    /*init {
        val productId = savedStateHandle.get<Int>("productId")!!
        if (productId != -1) {
            viewModelScope.launch {
                productRepository.getProductById(productId)?.let {
                    productName = it.productName
                    productPrice = it.productPrice
                    this@ProductViewModel.product = product
                }

            }
        }
    }*/

    val products = productRepository.getAllProducts()

    fun saveProductToDatabase(product: Product){
        viewModelScope.launch {
            productRepository.insertProduct(product) }
    }

    fun deleteProductFromDatabase(product: Product){
        viewModelScope.launch {
            productRepository.deleteProduct(product)
        }
    }

    fun getProductFromDatabaseById(productId: Int): Product {
        var product = Product("", "")
        viewModelScope.launch {
         product = productRepository.getProductById(productId)!!
        }
        return product
    }
}