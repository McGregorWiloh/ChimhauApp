package com.mcgregor.chimhauapp.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "product")
data class Product(

    @ColumnInfo(name = "product_name")
    val productName: String,
    @ColumnInfo(name = "product_price")
    val productPrice: String,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
)
