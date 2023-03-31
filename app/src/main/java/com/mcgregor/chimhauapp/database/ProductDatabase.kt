package com.mcgregor.chimhauapp.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mcgregor.chimhauapp.dao.ProductDao
import com.mcgregor.chimhauapp.models.Product


@Database(entities = [Product::class], version = 1, exportSchema = false)
abstract class ProductDatabase : RoomDatabase() {

    abstract val productDao: ProductDao

}