package com.mcgregor.chimhauapp.models

data class ProductTransaction(
    val product: Product,
    val productQuantity: Double,
    val productTotalAmount: Double
)
